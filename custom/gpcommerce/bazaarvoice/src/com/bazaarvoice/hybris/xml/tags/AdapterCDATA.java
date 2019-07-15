/**
 * 
 */
package com.bazaarvoice.hybris.xml.tags;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author christina romashchenko
 * 
 */
public class AdapterCDATA extends XmlAdapter<String, String>
{

	@Override
	public String marshal(final String arg0) throws Exception
	{
		return "<![CDATA[" + arg0.replaceAll("[^\\x20-\\x7e]", "") + "]]>";
	}

	@Override
	public String unmarshal(final String arg0) throws Exception
	{
		return arg0;
	}

}