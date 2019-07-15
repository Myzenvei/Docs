/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.v2.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.WriteListener;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.output.TeeOutputStream;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.gp.commerce.constants.YcommercewebservicesConstants;
import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.model.ReqResponseDataModel;

import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;



/**
 * @author sagdhingra
 *
 */
public class ReqRespLoggingFilter implements Filter
{

	private static final String DEFAULT_USER_SERVICE = "defaultUserService";

	private static final String DEFAULT_MODEL_SERVICE = "defaultModelService";

	private static final String QUERY_DATA = "queryData";

	private static final Logger logger = Logger.getLogger(ReqRespLoggingFilter.class);

	@Autowired
	private ModelService modelService;

	@Autowired
	private UserService userService;



	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException
	{
		try
		{
			//Activating master Tenant to access Hybris Resources
			Registry.activateMasterTenant();
			if (Boolean.valueOf(Config.getString(GpcommerceCoreConstants.DBLOGGER, GpcommerceCoreConstants.FALSE)).booleanValue()
					&& !userService.isAdmin(userService.getCurrentUser()))
			{
				//Fetching date from properties file
				final SimpleDateFormat dateFormat = new SimpleDateFormat(
						Config.getParameter(YcommercewebservicesConstants.DATEFORMAT));
				final HttpServletRequest httpServletRequest = (HttpServletRequest) request;
				final HttpServletResponse httpServletResponse = (HttpServletResponse) response;
				final Date startTime = new Date();
				((HttpServletRequest) request).setAttribute(YcommercewebservicesConstants.TRACEDATA, "");
				((HttpServletRequest) request).setAttribute(QUERY_DATA, "");
				final Map<String, String> requestMap = this.getTypesafeRequestMap(httpServletRequest);
				final BufferedRequestWrapper bufferedReqest = new BufferedRequestWrapper(httpServletRequest);
				final BufferedResponseWrapper bufferedResponse = new BufferedResponseWrapper(httpServletResponse);
				chain.doFilter(bufferedReqest, bufferedResponse);
				final Date endTime = new Date();
				Registry.activateMasterTenant();
				final ReqResponseDataModel reqResponseData = modelService.create(ReqResponseDataModel.class);
				reqResponseData.setRequestType(httpServletRequest.getMethod());
				setRequestParameter(reqResponseData, requestMap);
				reqResponseData.setRemoteAddress(httpServletRequest.getRemoteAddr());
				reqResponseData.setRequestUrl(httpServletRequest.getPathInfo());
				setRequestBody(reqResponseData, bufferedReqest);
				setResponseBody(reqResponseData, bufferedResponse);
				reqResponseData.setBeginTime(dateFormat.format(startTime));
				reqResponseData.setEndTime(dateFormat.format(endTime));

				ZonedDateTime now = ZonedDateTime.ofInstant(dateFormat.parse(dateFormat.format(endTime)).toInstant(),
						ZoneId.systemDefault());
				ZonedDateTime oldDate = ZonedDateTime.ofInstant(dateFormat.parse(dateFormat.format(startTime)).toInstant(),
						ZoneId.systemDefault());
				Duration duration = Duration.between(oldDate, now);
				reqResponseData.setDuration(String.valueOf(duration.toMillis()));
				final String traceData = (String) ((HttpServletRequest) request)
						.getAttribute(YcommercewebservicesConstants.TRACEDATA);
				final String queryData = (String) ((HttpServletRequest) request).getAttribute(QUERY_DATA);
				setTraceData(reqResponseData, traceData);
				setQueryData(reqResponseData, queryData);
				modelService.save(reqResponseData);
			}
			else
			{
				chain.doFilter(request, response);
			}
		}
		catch (final ModelSavingException mse)
		{
			logger.error("Error occurred while saving the reqResponseData", mse);
		}
		catch (final Exception a)
		{
			logger.error(a);
		}
	}


	private void setQueryData(ReqResponseDataModel reqResponseData, String queryData)
	{
		if (queryData.length() < 4000)
		{
			reqResponseData.setQueryData(queryData);
		}
		else if (queryData.length() >= 4000)
		{
			final String queryData1 = queryData.substring(0, 3999);
			reqResponseData.setQueryData(queryData1);
		}

	}


	private void setRequestParameter(final ReqResponseDataModel reqResponseData, final Map<String, String> requestMap)
	{
		if (requestMap != null && !requestMap.isEmpty())
		{
			reqResponseData.setRequestParameter(requestMap.toString().substring(0,
					((requestMap.toString().length() >= 4000) ? 3999 : requestMap.toString().length())));
		}
	}

	private void setResponseBody(final ReqResponseDataModel reqResponseData, final BufferedResponseWrapper bufferedResponse)
	{
		if (bufferedResponse.getContent() != null)
		{
			reqResponseData.setRespBody(bufferedResponse.getContent().trim().substring(0,
					((bufferedResponse.getContent().trim().length() >= 4000) ? 3999 : bufferedResponse.getContent().trim().length())));
		}
	}

	private void setRequestBody(final ReqResponseDataModel reqResponseData, final BufferedRequestWrapper bufferedReqest)
			throws IOException
	{
		if (bufferedReqest.getRequestBody() != null)
		{
			reqResponseData.setReqBody(
					bufferedReqest.getRequestBody().trim().substring(0, ((bufferedReqest.getRequestBody().trim().length() >= 4000)
							? 3999 : bufferedReqest.getRequestBody().trim().length())));
		}
	}

	private void setTraceData(final ReqResponseDataModel reqResponseData, final String traceData)
	{
		if (traceData.length() < 4000)
		{
			reqResponseData.setOutboundReqResponseData(traceData);
		}
		else if (traceData.length() >= 4000)
		{
			final String traceData1 = traceData.substring(0, 3999);
			reqResponseData.setOutboundReqResponseData(traceData1);
			final String traceData2 = traceData.substring(4000, ((traceData.length() > 8000) ? 7999 : traceData.length()));
			reqResponseData.setOutboundReqResponseData1(traceData2);
		}
	}


	private final Map<String, String> getTypesafeRequestMap(final HttpServletRequest request)
	{
		final Map<String, String> typesafeRequestMap = new HashMap<String, String>();
		final Enumeration<?> requestParamNames = request.getParameterNames();
		while (requestParamNames.hasMoreElements())
		{
			final String requestParamName = (String) requestParamNames.nextElement();
			final String requestParamValue = request.getParameter(requestParamName);
			typesafeRequestMap.put(requestParamName, requestParamValue);
		}

		final Enumeration<?> requestHeaderNames = request.getHeaderNames();
		while (requestHeaderNames.hasMoreElements())
		{
			final String requestParamName = (String) requestHeaderNames.nextElement();
			final String requestParamValue = request.getHeader(requestParamName);
			typesafeRequestMap.put(requestParamName, requestParamValue);
		}

		return typesafeRequestMap;
	}




	private static final class BufferedRequestWrapper extends HttpServletRequestWrapper
	{

		private ByteArrayInputStream bais = null;
		private ByteArrayOutputStream baos = null;
		private BufferedServletInputStream bsis = null;
		private byte[] buffer = null;


		public BufferedRequestWrapper(final HttpServletRequest req) throws IOException
		{
			super(req);
			// Read InputStream and store its content in a buffer.
			final InputStream is = req.getInputStream();
			this.baos = new ByteArrayOutputStream();
			final byte buf[] = new byte[1024];
			int letti;
			while ((letti = is.read(buf)) > 0)
			{
				this.baos.write(buf, 0, letti);
			}
			this.buffer = this.baos.toByteArray();
		}


		@Override
		public ServletInputStream getInputStream()
		{
			this.bais = new ByteArrayInputStream(this.buffer);
			this.bsis = new BufferedServletInputStream(this.bais);
			return this.bsis;
		}



		String getRequestBody() throws IOException
		{
			final BufferedReader reader = new BufferedReader(new InputStreamReader(this.getInputStream()));
			String line = null;
			final StringBuilder inputBuffer = new StringBuilder();
			do
			{
				line = reader.readLine();
				if (null != line)
				{
					inputBuffer.append(line.trim());
				}
			}
			while (line != null);
			reader.close();
			return inputBuffer.toString().trim();
		}

	}


	private static final class BufferedServletInputStream extends ServletInputStream
	{

		private final ByteArrayInputStream bais;

		public BufferedServletInputStream(final ByteArrayInputStream bais)
		{
			this.bais = bais;
		}

		@Override
		public int available()
		{
			return this.bais.available();
		}

		@Override
		public int read()
		{
			return this.bais.read();
		}

		@Override
		public int read(final byte[] buf, final int off, final int len)
		{
			return this.bais.read(buf, off, len);
		}

		@Override
		public boolean isFinished() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isReady() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void setReadListener(ReadListener arg0) {
			// TODO Auto-generated method stub
			
		}


	}

	public class TeeServletOutputStream extends ServletOutputStream
	{

		private final TeeOutputStream targetStream;

		public TeeServletOutputStream(final OutputStream one, final OutputStream two)
		{
			targetStream = new TeeOutputStream(one, two);
		}

		@Override
		public void write(final int arg0) throws IOException
		{
			this.targetStream.write(arg0);
		}

		@Override
		public void flush() throws IOException
		{
			super.flush();
			this.targetStream.flush();
		}

		@Override
		public void close() throws IOException
		{
			super.close();
			this.targetStream.close();
		}

		@Override
		public boolean isReady() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void setWriteListener(WriteListener arg0) {
			// TODO Auto-generated method stub
			
		}
	}



	public class BufferedResponseWrapper implements HttpServletResponse
	{

		HttpServletResponse original;
		TeeServletOutputStream tee;
		ByteArrayOutputStream bos;

		public BufferedResponseWrapper(final HttpServletResponse response)
		{
			original = response;
		}

		public String getContent()
		{
			return (bos != null ? bos.toString() : new String());
		}

		public PrintWriter getWriter() throws IOException
		{
			return original.getWriter();
		}

		public ServletOutputStream getOutputStream() throws IOException
		{
			if (tee == null)
			{
				bos = new ByteArrayOutputStream();
				tee = new TeeServletOutputStream(original.getOutputStream(), bos);
			}
			return tee;

		}

		@Override
		public String getCharacterEncoding()
		{
			return original.getCharacterEncoding();
		}

		@Override
		public String getContentType()
		{
			return original.getContentType();
		}

		@Override
		public void setCharacterEncoding(final String charset)
		{
			original.setCharacterEncoding(charset);
		}

		@Override
		public void setContentLength(final int len)
		{
			original.setContentLength(len);
		}

		@Override
		public void setContentType(final String type)
		{
			original.setContentType(type);
		}

		@Override
		public void setBufferSize(final int size)
		{
			original.setBufferSize(size);
		}

		@Override
		public int getBufferSize()
		{
			return original.getBufferSize();
		}

		@Override
		public void flushBuffer() throws IOException
		{
			tee.flush();
		}

		@Override
		public void resetBuffer()
		{
			original.resetBuffer();
		}

		@Override
		public boolean isCommitted()
		{
			return original.isCommitted();
		}

		@Override
		public void reset()
		{
			original.reset();
		}

		@Override
		public void setLocale(final Locale loc)
		{
			original.setLocale(loc);
		}

		@Override
		public Locale getLocale()
		{
			return original.getLocale();
		}

		@Override
		public void addCookie(final Cookie cookie)
		{
			original.addCookie(cookie);
		}

		@Override
		public boolean containsHeader(final String name)
		{
			return original.containsHeader(name);
		}

		@Override
		public String encodeURL(final String url)
		{
			return original.encodeURL(url);
		}

		@Override
		public String encodeRedirectURL(final String url)
		{
			return original.encodeRedirectURL(url);
		}

		@SuppressWarnings("deprecation")
		@Override
		public String encodeUrl(final String url)
		{
			return original.encodeUrl(url);
		}

		@SuppressWarnings("deprecation")
		@Override
		public String encodeRedirectUrl(final String url)
		{
			return original.encodeRedirectUrl(url);
		}

		@Override
		public void sendError(final int sc, final String msg) throws IOException
		{
			original.sendError(sc, msg);
		}

		@Override
		public void sendError(final int sc) throws IOException
		{
			original.sendError(sc);
		}

		@Override
		public void sendRedirect(final String location) throws IOException
		{
			original.sendRedirect(location);
		}

		@Override
		public void setDateHeader(final String name, final long date)
		{
			original.setDateHeader(name, date);
		}

		@Override
		public void addDateHeader(final String name, final long date)
		{
			original.addDateHeader(name, date);
		}

		@Override
		public void setHeader(final String name, final String value)
		{
			original.setHeader(name, value);
		}

		@Override
		public void addHeader(final String name, final String value)
		{
			original.addHeader(name, value);
		}

		@Override
		public void setIntHeader(final String name, final int value)
		{
			original.setIntHeader(name, value);
		}

		@Override
		public void addIntHeader(final String name, final int value)
		{
			original.addIntHeader(name, value);
		}

		@Override
		public void setStatus(final int sc)
		{
			original.setStatus(sc);
		}

		@SuppressWarnings("deprecation")
		@Override
		public void setStatus(final int sc, final String sm)
		{
			original.setStatus(sc, sm);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see javax.servlet.http.HttpServletResponse#getHeader(java.lang.String)
		 */
		@Override
		public String getHeader(final String arg0)
		{
			// YTODO Auto-generated method stub
			return null;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see javax.servlet.http.HttpServletResponse#getHeaderNames()
		 */
		@Override
		public Collection<String> getHeaderNames()
		{
			// YTODO Auto-generated method stub
			return null;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see javax.servlet.http.HttpServletResponse#getHeaders(java.lang.String)
		 */
		@Override
		public Collection<String> getHeaders(final String arg0)
		{
			// YTODO Auto-generated method stub
			return null;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see javax.servlet.http.HttpServletResponse#getStatus()
		 */
		@Override
		public int getStatus()
		{
			// YTODO Auto-generated method stub
			return original.getStatus();
		}

		@Override
		public void setContentLengthLong(long arg0) {
			// TODO Auto-generated method stub
			
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy()
	{
		// YTODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(final FilterConfig arg0) throws ServletException
	{
		modelService = (ModelService) WebApplicationContextUtils.getRequiredWebApplicationContext(arg0.getServletContext())
				.getBean(DEFAULT_MODEL_SERVICE);

		userService = (UserService) WebApplicationContextUtils.getRequiredWebApplicationContext(arg0.getServletContext())
				.getBean(DEFAULT_USER_SERVICE);
	}

}
