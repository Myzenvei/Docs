/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.core.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.imageio.ImageIO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.beans.factory.annotation.Required;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.enums.CertificationsEnum;
import com.gp.commerce.core.exceptions.GPWishlistException;
import com.gp.commerce.facades.product.data.DataSheetsData;
import com.gp.commerce.facades.product.data.GpCertificationsData;
import com.gp.commerce.facades.product.data.ProductResourcesVideosData;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commercefacades.product.data.ClassificationData;
import de.hybris.platform.commercefacades.product.data.FeatureData;
import de.hybris.platform.commercefacades.product.data.FeatureValueData;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.ProductReferenceData;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.util.Config;

public class GPWishlistUtil {
	private static final Logger LOG = Logger.getLogger(GPWishlistUtil.class);
	private BaseSiteService baseSiteService;
	private ConfigurationService configurationService;
	private SessionService sessionService;
	private static final String RETAIL_SOLD_TO = "gpxpress.retail.soldToId";
	private static final String ALL_TYPE = "all";

	public void addImagesToZip(FileOutputStream fout, File sourceFile, String resolution) throws IOException {
		// Check resolution and write images to zip.
		try (ZipOutputStream zout = new ZipOutputStream(fout);) {
			File[] files = sourceFile.listFiles();
			if (files.length >= 1) {
				for (File file : files) {
					if (!ALL_TYPE.equals(resolution)) {
						BufferedImage bufferedImage = ImageIO.read(file);
						if (bufferedImage.getWidth() != Integer.parseInt(resolution)
								&& bufferedImage.getHeight() != Integer.parseInt(resolution)) {
							file.delete();
						}
					}
					if (file.exists()) {
						try (FileInputStream fin = new FileInputStream(file);) {
							zout.putNextEntry(new ZipEntry(file.getName()));
							int ln;
							int fileLength = (int) file.length();
							byte[] b = new byte[fileLength];
							while ((ln = fin.read(b)) > 0) {
								zout.write(b, 0, ln);
							}
							zout.closeEntry();
						}
					}
				}
			} else {
				throw new GPWishlistException("3333", "No images are currently available.");
			}
		}
	}
	
	//Delete the temp files after creation of zip.
		public void delete(File sourceFile) {
			String[] sou = sourceFile.list();
			for (String s : sou) {
				File currentFile = new File(sourceFile.getPath(), s);
				currentFile.delete();
			}
			sourceFile.delete();
		}
		
		public InputStream createExcelFile(final List<ProductData> products, List<String> files,String zipFileName) throws IOException
		{
			final String directoryPath = Config.getParameter(GpcommerceCoreConstants.WISHLIST_EXPORT_ZIP); 
			File zipFile = new File(directoryPath, zipFileName);
			FileOutputStream fos = new FileOutputStream(zipFile);
			ZipOutputStream zipOut = new ZipOutputStream(fos);
			
			try {
				files.forEach(file -> {
					HSSFWorkbook workbook = null;
					ZipEntry ze = null;
					try
					{
					
					final File input = new File(directoryPath, file);
					 if(!input.exists())
		             {
		                 //If directories are not available then create it
		                 File parentDirectory = input.getParentFile();
		                 if (null != parentDirectory)
		                 {
		                	 parentDirectory.mkdirs();
		                 }
		                 input.createNewFile();
		             }
					ze = new ZipEntry(file);
					workbook = createWorkBook(products,input);
					zipOut.putNextEntry(ze);
					workbook.write(zipOut);
	                
					}
					catch (final IOException ioex)
					{
						LOG.error("IOException occurred while creating excel attachment ", ioex);
					}
					catch (final Exception ex)
					{
						LOG.error("Exception occurred while populating excel with data", ex);
					}
					finally
					{
						try
						{

							if (workbook != null)
							{
								workbook.close();
							}
							
							if(ze != null)
							{
								zipOut.closeEntry();
							}
						}
						catch (final IOException ex)
						{
							LOG.error("streams not closed", ex);
						}
					}
				});
				}
				finally
				{
					try
					{
						if(zipOut != null)
						{
							zipOut.close();
						}
					}
					catch (final IOException ex)
					{
						LOG.error("ZipOut not closed", ex);
					}
				}
				return new FileInputStream(zipFile);
			}
			
			
			public HSSFWorkbook createWorkBook(final List<ProductData> products, File input) throws IOException
			{
				final HSSFWorkbook workbook = new HSSFWorkbook();
					final HSSFSheet sheet = workbook.createSheet();
					final HSSFCellStyle style = workbook.createCellStyle();
					style.setWrapText(true);
					style.setAlignment(HorizontalAlignment.CENTER);
					HSSFFont font = workbook.createFont();
					font.setBold(true);
					style.setFont(font);
					if(input != null)
					{
						LOG.debug("Writing product details on the Sheet for products :" + products.size());
						if(input.getName().startsWith("Multi"))
						{
							LOG.debug("Creating Multi Excel Sheet");
							createHeaderForMultiSheet(sheet,style);
							for (final ProductData product : products)
							{
								createProductRowsForMultiSheet( workbook, sheet, style, product);
							}
						}
						else if(input.getName().startsWith("Product"))
						{
							if(ifRetailSoldTo())
							{
								LOG.debug("Creating Retail_Product Excel Sheet");
								HSSFRow headerRow = createHeaderForRetailProductSheet(sheet,style);
								int startRow = 1;
								for (final ProductData product : products)
								{
									createRowsForRetailProductSheet( workbook, sheet, style, product,startRow,headerRow);
									++startRow;
								}
							}
							else
							{
								LOG.debug("Creating B2B Product Excel Sheet");
								HSSFRow headerRow = createHeaderForB2BProductSheet(sheet,style);
								int startRow = 1;
								for (final ProductData product : products)
								{
									createRowsForB2BProductSheet( workbook, sheet, style, product,startRow,headerRow);
									++startRow;
								}
							}
							
						}
						else
						{
							LOG.debug("Creating Related Excel Sheet");
							createHeaderForRelatedSheet(sheet,style);
							for (final ProductData product : products)
							{
								createProductRowsForRelatedSheet(workbook, sheet, style, product);
							}
						}
					}
				return workbook;
			}

			public void createHeaderForMultiSheet(final HSSFSheet sheet, HSSFCellStyle style) {
				String multiProductHeader = configurationService.getConfiguration().getString("multi.product.headers");
				List<String> header = splitHeader(multiProductHeader);
				int startColumn = 0;
				int startRow = 0;
				HSSFRow headerRow = sheet.createRow(startRow);
				headerRow.setRowStyle(style);
				for(int i= 0; i < header.size(); i++)
				{
					headerRow.createCell(startColumn).setCellValue(header.get(i));
					++startColumn;
				}
			}
			
			public void createProductRowsForMultiSheet(HSSFWorkbook workbook, HSSFSheet sheet, HSSFCellStyle style,
					ProductData product) {
				int startColumn = 0;
				List<String> features = product.getFeatureList();
				LOG.debug("Features :" + features.size());
				if(CollectionUtils.isNotEmpty(features)) 
				{
					for (int i= 0 ; i < features.size();i++)
					{
						int lastRowNum = sheet.getLastRowNum();
						HSSFRow row = sheet.createRow(++lastRowNum);
						row.createCell(startColumn).setCellValue(product.getCode());
						row.createCell(++startColumn).setCellValue("Features");
						row.createCell(++startColumn).setCellValue(features.get(i));
						startColumn= 0;
					}
				}
				DataSheetsData dataSheetsList = product.getDataSheets();
				if(null != dataSheetsList && null != dataSheetsList.getDataSheets()) 
				{
					List<ImageData> dataSheets = dataSheetsList.getDataSheets();
					LOG.debug("DataSheetsData:" + dataSheets.size());
					for (int i= 0 ; i < dataSheets.size();i++)
					{
						if(StringUtils.isNotEmpty(dataSheets.get(i).getResourceURL()))
						{
							int lastRowNum = sheet.getLastRowNum();
							HSSFRow row = sheet.createRow(++lastRowNum);
							row.createCell(startColumn).setCellValue(product.getCode());
							row.createCell(++startColumn).setCellValue("Links");
							row.createCell(++startColumn).setCellValue(dataSheets.get(i).getResourceURL());
							startColumn= 0;
						}
					}
				}
				ProductResourcesVideosData productResourceVideos = product.getProductResourceVideos();
				if(null != productResourceVideos && null != productResourceVideos.getVideo()) 
				{
					List<ImageData> videos = productResourceVideos.getVideo();
					LOG.debug("ProductResourcesVideosData:" + videos.size());
					for (int i= 0 ; i < videos.size();i++)
					{
						if(StringUtils.isNotEmpty(videos.get(i).getResourceURL()))
						{
							int lastRowNum = sheet.getLastRowNum();
							HSSFRow row = sheet.createRow(++lastRowNum);
							row.createCell(startColumn).setCellValue(product.getCode());
							row.createCell(++startColumn).setCellValue("Links");
							row.createCell(++startColumn).setCellValue(videos.get(i).getResourceURL());
							startColumn= 0;
						}
					}
				}
			}

			public void createHeaderForRelatedSheet(HSSFSheet sheet, HSSFCellStyle style) {
				String relatedProductHeader = configurationService.getConfiguration().getString("related.product.headers");
				List<String> header = splitHeader(relatedProductHeader);
				int startColumn = 0;
				int startRow = 0;
				HSSFRow headerRow = sheet.createRow(startRow);
				headerRow.setRowStyle(style);
				for(int i= 0; i < header.size(); i++)
				{
					headerRow.createCell(startColumn).setCellValue(header.get(i));
					++startColumn;
				}		
			}
			
			public void createProductRowsForRelatedSheet(HSSFWorkbook workbook, HSSFSheet sheet, HSSFCellStyle style,
					ProductData product) {
				int startColumn = 0;
				List<ProductReferenceData> productReferences = product.getProductReferences();
				LOG.debug("ProductReferenceData :" + productReferences);
				if(CollectionUtils.isNotEmpty(productReferences))
				{
					for(int i=0;i < productReferences.size();i++)
					{
						int lastRowNum = sheet.getLastRowNum();
						final HSSFRow row = sheet.createRow(++lastRowNum);
						row.createCell(startColumn).setCellValue(product.getCode());
						row.createCell(++startColumn).setCellValue(productReferences.get(i).getReferenceType().getCode());
						row.createCell(++startColumn).setCellValue(productReferences.get(i).getTarget().getCode());
						row.createCell(++startColumn).setCellValue(product.getName());
						row.createCell(++startColumn).setCellValue(productReferences.get(i).getTarget().getName());
						startColumn= 0;
					}
				}
			}
			
			public HSSFRow createHeaderForB2BProductSheet(HSSFSheet sheet, HSSFCellStyle style) {
				String b2bProductHeader = configurationService.getConfiguration().getString("csv.b2b.product.headers");
				List<String> header = splitHeader(b2bProductHeader);
				int startColumn = 0;
				int startRow = 0;
				HSSFRow headerRow = sheet.createRow(startRow);
				headerRow.setRowStyle(style);
				for(int i= 0; i < header.size(); i++)
				{
					headerRow.createCell(startColumn).setCellValue(header.get(i));
					++startColumn;
				}
				
				return headerRow;
			}
			
			public void createRowsForB2BProductSheet(HSSFWorkbook workbook, HSSFSheet sheet, HSSFCellStyle style,
					ProductData product,int startRow, HSSFRow headerRow) {
				int startColumn = 0;
				boolean isCertified;
				HSSFRow row = sheet.createRow(startRow);
				row.createCell(startColumn);
				row.getCell(startColumn).setCellValue(product.getCode());
				row.createCell(++startColumn).setCellValue("");
				row.createCell(++startColumn).setCellValue(product.getCaseGtin());
				row.createCell(++startColumn).setCellValue(product.getUpc());
				startColumn = createClassificationRow(row,startColumn,product.getClassifications(),"Replaces Item");
				row.createCell(++startColumn).setCellValue(product.getReplacedBy());
				row.createCell(++startColumn).setCellValue(product.getMaterialStatus());
				row.createCell(++startColumn).setCellValue(product.getSummary());
				String prodDescription = product.getName();
				if(StringUtils.isNotEmpty(prodDescription)) {
					prodDescription = prodDescription.replaceAll("\\<.*?\\>", "");
				}
				row.createCell(++startColumn).setCellValue(prodDescription);
				row.createCell(++startColumn).setCellValue(product.getBrandOwner());
				row.createCell(++startColumn).setCellValue(product.getSubbrand());
				row.createCell(++startColumn).setCellValue(product.getBrandTrademark());
				startColumn = createClassificationRow(row,startColumn,product.getClassifications(),"Color");
				row.createCell(++startColumn).setCellValue(product.getAssetCode());
				row.createCell(++startColumn).setCellValue(product.getUnspsc());
				row.createCell(++startColumn).setCellValue(product.getUnspscdesc());
				List<String> buyMultAttributes = getClassificationAttributes("product.b2b.buyMult.attributes");
				if(null != buyMultAttributes) {
				for(int i=0;i<buyMultAttributes.size();i++) {
					startColumn = createClassificationRow(row,startColumn,product.getClassifications(),buyMultAttributes.get(i));	
				}
				}
				if(null != product.getMinOrderQuantity())
				{
					row.createCell(++startColumn).setCellValue(product.getMinOrderQuantity());
				}
				else
				{
					row.createCell(++startColumn).setCellValue("");
				}
				List<String> eachAttributes = getClassificationAttributes("product.b2b.each.attributes");
				if(null != eachAttributes) {
				for(int i=0;i<eachAttributes.size();i++) {
					startColumn = createClassificationRow(row,startColumn,product.getClassifications(),eachAttributes.get(i));	
				}
				}
				row.createCell(++startColumn).setCellValue(product.getItemPerEachUOM());
				row.createCell(++startColumn).setCellValue(product.getCaseTotal());
				row.createCell(++startColumn).setCellValue(product.getCaseTotalUOM());
				row.createCell(++startColumn).setCellValue(product.getProductWidth());
				row.createCell(++startColumn).setCellValue(product.getProductWidthUOM());
				List<String> shipAttributes = getClassificationAttributes("product.b2b.ship.attributes");
				if(null != shipAttributes) {
				for(int i=0;i<shipAttributes.size();i++) {
					startColumn = createClassificationRow(row,startColumn,product.getClassifications(),shipAttributes.get(i));	
				}
				}
				List<String> b2bstatAttributes = getClassificationAttributes("product.b2b.stat.attributes");
				if(null != b2bstatAttributes) {
				for(int i=0;i<b2bstatAttributes.size();i++) {
					startColumn = createClassificationRow(row,startColumn,product.getClassifications(),b2bstatAttributes.get(i));	
				}
				}
				row.createCell(++startColumn).setCellValue(product.getProductLengthUOM());
				row.createCell(++startColumn).setCellValue(product.getProductHeight());
				row.createCell(++startColumn).setCellValue(product.getProductLength());
				row.createCell(++startColumn).setCellValue(product.getProductHeightUOM());
				row.createCell(++startColumn).setCellValue(product.getEachNetWeight());
				row.createCell(++startColumn).setCellValue(product.getEachGrossWeight());
				row.createCell(++startColumn).setCellValue(product.getEachWeightUOM());
				row.createCell(++startColumn).setCellValue(product.getEachProdWidth());
				row.createCell(++startColumn).setCellValue(product.getEachProdLength());
				row.createCell(++startColumn).setCellValue(product.getEachProdHeight());
				row.createCell(++startColumn).setCellValue(product.getEachDimensionsUOM());
				row.createCell(++startColumn).setCellValue(product.getEachvolume());
				row.createCell(++startColumn).setCellValue(product.getEachvolumeUOM());
				startColumn = createClassificationRow(row,startColumn,product.getClassifications(),"Capacity");
				row.createCell(++startColumn).setCellValue(product.getCaseWidth());
				row.createCell(++startColumn).setCellValue(product.getCaseLength());
				row.createCell(++startColumn).setCellValue(product.getCaseHeight());
				row.createCell(++startColumn).setCellValue(product.getCaseDimensionsUOM());
				row.createCell(++startColumn).setCellValue(product.getCaseVolume());
				//Case Cube UOM
				row.createCell(++startColumn).setCellValue("");
				row.createCell(++startColumn).setCellValue(product.getCaseNetWeight());
				row.createCell(++startColumn).setCellValue(product.getCaseGrossWeight());
				row.createCell(++startColumn).setCellValue(product.getCaseWeightUOM());
				startColumn = createClassificationRow(row,startColumn,product.getClassifications(),"TI-QTY/Layer");
				startColumn = createClassificationRow(row,startColumn,product.getClassifications(),"HI-Layers/Unit");
				row.createCell(++startColumn).setCellValue(product.getUnitQty());
				//Unit Floor Length,Unit Floor Width,Unit Floor Height,Unit Dimensions Uom
				row.createCell(++startColumn).setCellValue("");
				row.createCell(++startColumn).setCellValue("");
				row.createCell(++startColumn).setCellValue("");
				row.createCell(++startColumn).setCellValue("");
				row.createCell(++startColumn).setCellValue(product.getSellingstmt());
				row.createCell(++startColumn).setCellValue(product.getDescription());
				//European Commission Certified,EPA CPG Compliant,Min PCW
				String certified = configurationService.getConfiguration().getString("certificate.exists.true");
				isCertified = checkIfCertified(product,CertificationsEnum.ECC.getCode());
				startColumn = createCertification(startColumn, row, certified, isCertified);
				isCertified = checkIfCertified(product,CertificationsEnum.EPA.getCode());
				startColumn = createCertification(startColumn, row, certified, isCertified);
				row.createCell(++startColumn).setCellValue(product.getMinPCW());
				//Min Recycled,EcoLogo,ECOLOGO CCD,Helps Reduce Waste,LEED OM
				row.createCell(++startColumn).setCellValue(product.getMinRecycled());
				isCertified = checkIfCertified(product,CertificationsEnum.ECO_LOGO.getCode());
				startColumn = createCertification(startColumn, row, certified, isCertified);
				row.createCell(++startColumn).setCellValue(product.getEcologoCCD());
				isCertified = checkIfCertified(product,CertificationsEnum.WASTE_REDUCING.getCode());
				startColumn = createCertification(startColumn, row, certified, isCertified);
				row.createCell(++startColumn).setCellValue(product.getLeedOM());
				//LEED MR Criteria,USGBC LEED Eligible,Alternative Materials,LEED IEQ Criteria,Green Seal
				row.createCell(++startColumn).setCellValue(product.getLeedMRCriteria());
				isCertified = checkIfCertified(product,CertificationsEnum.USGBC_LEED.getCode());
				startColumn = createCertification(startColumn, row, certified, isCertified);
				String altExists = configurationService.getConfiguration().getString("altMaterials.exists.true");
				String altNotExists = configurationService.getConfiguration().getString("altMaterials.exists.false");
				if(Boolean.TRUE.equals(product.getAltMaterials())) 
				{
					row.createCell(++startColumn).setCellValue(altExists);
				}
				else 
				{
					row.createCell(++startColumn).setCellValue(altNotExists);
				}
				row.createCell(++startColumn).setCellValue(product.getLeedIEQCriteria());
				isCertified = checkIfCertified(product,CertificationsEnum.GREEN_SEAL.getCode());
				startColumn = createCertification(startColumn, row, certified, isCertified);
				//LEED INV Criteria,SFI Sourcing and Chain of Custody Certified,BPI Certified,Forest Stewardship Council FSC,How2Recycle
				row.createCell(++startColumn).setCellValue(product.getLeedINVCriteria());
				isCertified = checkIfCertified(product,CertificationsEnum.SFI.getCode());
				startColumn = createCertification(startColumn, row, certified, isCertified);
				isCertified = checkIfCertified(product,CertificationsEnum.BPI.getCode());
				startColumn = createCertification(startColumn, row, certified, isCertified);
				isCertified = checkIfCertified(product,CertificationsEnum.FSC.getCode());
				startColumn = createCertification(startColumn, row, certified, isCertified);
				isCertified = checkIfCertified(product,CertificationsEnum.HOW_2_RECYCLE.getCode());
				startColumn = createCertification(startColumn, row, certified, isCertified);
				//USDA Certified Biobased Product,Start Availability Date,Freight Class
				isCertified = checkIfCertified(product,CertificationsEnum.USDA.getCode());
				startColumn = createCertification(startColumn, row, certified, isCertified);
				row.createCell(++startColumn).setCellValue("");
				row.createCell(++startColumn).setCellValue("");
				startColumn = createClassificationRow(row,startColumn,product.getClassifications(),"Core Size");
				//Folded WL
				row.createCell(++startColumn).setCellValue("");
				startColumn = createClassificationRow(row,startColumn,product.getClassifications(),"Ply");
				//DSL Lease Required,Warranty,Lease
				row.createCell(++startColumn).setCellValue("");
				row.createCell(++startColumn).setCellValue("");
				row.createCell(++startColumn).setCellValue("");
				startColumn = createClassificationRow(row,startColumn,product.getClassifications(),"Roll Diameter");
				//Search Category,Coating
				row.createCell(++startColumn).setCellValue("");
				row.createCell(++startColumn).setCellValue("");
				startColumn = createClassificationRow(row,startColumn,product.getClassifications(),"Kosher");
				//Minimum Product Lifespan,Storage Handling Temp Max,Storage Handling Temp Min,Storage Handling Temp UOM
				row.createCell(++startColumn).setCellValue("");
				row.createCell(++startColumn).setCellValue("");
				row.createCell(++startColumn).setCellValue("");
				row.createCell(++startColumn).setCellValue("");
			}
			/**
			 * Populate the column values based on isCertified
			 * @param startColumn
			 * @param row
			 * @param certified
			 * @param isCertified
			 * @return column value
			 */
			public int createCertification(int startColumn, HSSFRow row, String certified, boolean isCertified) {
				if(isCertified && StringUtils.isNotEmpty(certified))
				{
					row.createCell(++startColumn).setCellValue(certified);
				}
				else
				{
					row.createCell(++startColumn).setCellValue("");
				}
				return startColumn;
			}
			/**
			 * Check if product has certifications associated to it.
			 * @param product
			 * @param certificationCode
			 * @return boolean value
			 */
			public boolean checkIfCertified(ProductData product, String certificationCode) {
				if(null != product.getGpCertifications()) {
				for(GpCertificationsData gpCertification :product.getGpCertifications()) {
					if(gpCertification.getId().equalsIgnoreCase(certificationCode)) {
						return true;
					}
				}
				}
				return false;
			}

			/**
			 * Create classification data mapping
			 * @param row
			 * @param startColumn
			 * @param classifications
			 * @param classificationAttr
			 * @return startColumn
			 */
			public int createClassificationRow(HSSFRow row, int startColumn,
					Collection<ClassificationData> classifications, String classificationAttr) {
				if(CollectionUtils.isNotEmpty(classifications))
				{
					row.createCell(++startColumn).setCellValue("");
					LOG.debug("Classifications size : " + classifications.size() + " and classifications are :"+ classifications);
					for(ClassificationData classData :classifications)
					{
						Collection<FeatureData> features = classData.getFeatures();
						if(CollectionUtils.isNotEmpty(features)){
							for(FeatureData feature :features)
							{
								if(feature != null && feature.getName().equals(classificationAttr))
								{
									LOG.debug("Feature Name is: " +feature.getName()+ " and classificationAttr is: " +classificationAttr);
									setFeatureData(startColumn, row, feature);
								}
							}
						}
					}
				}
				return startColumn;
			}

			/**
			 * Sets retrieved classification feature value on the cell
			 * @param startColumn
			 * @param row
			 * @param feature
			 */
			public void setFeatureData(int startColumn, HSSFRow row, FeatureData feature) {
				Collection<FeatureValueData> featureValues = feature.getFeatureValues();
				if(CollectionUtils.isNotEmpty(featureValues)) {
					for (FeatureValueData featureValue :featureValues)
					{
						row.getCell(startColumn).setCellValue(featureValue.getValue());
						LOG.debug("Feature Value :" + featureValue.getValue() + "start column "+ startColumn);
					}
				}
			}
			
			/**
			 * Gets the value for the given classification attribute
			 * @param classifications
			 * @param classificationAttr
			 * @return featureValue
			 */
			public String getClassificationData(Collection<ClassificationData> classifications, String classificationAttr) {
				String featureValue="";
				if(CollectionUtils.isNotEmpty(classifications) && StringUtils.isNotEmpty(classificationAttr))
				{
					LOG.debug("Classifications size : " + classifications.size() + " and classifications are :"+ classifications);
					for(ClassificationData classData :classifications)
					{
						
						Collection<FeatureData> features = classData.getFeatures();
						for(FeatureData feature :features)
						{
							if(feature.getName().equals(classificationAttr))
							{
							LOG.debug("Feature Name is: " +feature.getName()+ " and classificationAttr is: " +classificationAttr);
							return getSplitFeatureData(feature);
							}	
						}
					}
				}
				LOG.debug("Value1:" + featureValue);
				return featureValue;
			}
			
			/**
			 * Splits the given feature value
			 * @param feature
			 * @return value
			 */
			public String getSplitFeatureData(FeatureData feature) {
				Collection<FeatureValueData> featureValues = feature.getFeatureValues();
				String value="";
				if(CollectionUtils.isNotEmpty(featureValues)) {
					for (FeatureValueData featureValue :featureValues)
					{
						LOG.debug("Feature Value :" + featureValue.getValue());
						value= featureValue.getValue();
					}
				}
				LOG.debug("Value2:" + value);
				return value;
			}

			
			public HSSFRow createHeaderForRetailProductSheet(HSSFSheet sheet, HSSFCellStyle style) {
				String retailProductHeader = configurationService.getConfiguration().getString("retail.product.headers");
				List<String> header = splitHeader(retailProductHeader);
				int startColumn = 0;
				int startRow = 0;
				HSSFRow headerRow = sheet.createRow(startRow);
				headerRow.setRowStyle(style);
				for(int i= 0; i < header.size(); i++)
				{
					headerRow.createCell(startColumn).setCellValue(header.get(i));
					++startColumn;
				}
				return headerRow;
			}
			
			public void createRowsForRetailProductSheet(HSSFWorkbook workbook, HSSFSheet sheet, HSSFCellStyle style,
					ProductData product, int startRow, HSSFRow headerRow) {
				int startColumn = 0;
				HSSFRow row = sheet.createRow(startRow);
				row.createCell(startColumn);
				row.getCell(startColumn).setCellValue(product.getCode());
				row.createCell(++startColumn).setCellValue(product.getCaseGtin());
				row.createCell(++startColumn).setCellValue(product.getUpc());
				startColumn = createClassificationRow(row,startColumn,product.getClassifications(),"Replaces Item");
				row.createCell(++startColumn).setCellValue(product.getReplacedBy());
				row.createCell(++startColumn).setCellValue(product.getMaterialStatus());
				String prodDescription = product.getName();
				if(StringUtils.isNotEmpty(prodDescription)) {
				prodDescription = prodDescription.replaceAll("\\<.*?\\>", "");
				}
				row.createCell(++startColumn).setCellValue(prodDescription);
				row.createCell(++startColumn).setCellValue(product.getBrandOwner());
				row.createCell(++startColumn).setCellValue(product.getSubbrand());
				row.createCell(++startColumn).setCellValue(product.getBrandTrademark());
				row.createCell(++startColumn).setCellValue(product.getAssetCode());
				row.createCell(++startColumn).setCellValue(product.getUnspsc());
				row.createCell(++startColumn).setCellValue(product.getUnspscdesc());
				List<String> buyMultAttributes = getClassificationAttributes("product.retail.buyMult.attributes");
				if(null != buyMultAttributes) {
				for(int i=0;i<buyMultAttributes.size();i++) {
					startColumn = createClassificationRow(row,startColumn,product.getClassifications(),buyMultAttributes.get(i));	
				}
				}
				if(null != product.getMinOrderQuantity())
				{
					row.createCell(++startColumn).setCellValue(product.getMinOrderQuantity());
				}
				else
				{
					row.createCell(++startColumn).setCellValue("");
				}
				List<String> eachAttributes = getClassificationAttributes("product.retail.each.attributes");
				if(null != eachAttributes) {
				for(int i=0;i<eachAttributes.size();i++) {
					startColumn = createClassificationRow(row,startColumn,product.getClassifications(),eachAttributes.get(i));	
				}
				}
				row.createCell(++startColumn).setCellValue(product.getItemPerEachUOM());
				row.createCell(++startColumn).setCellValue(product.getCaseTotal());
				row.createCell(++startColumn).setCellValue(product.getCaseTotalUOM());
				row.createCell(++startColumn).setCellValue(product.getProductWidth());
				row.createCell(++startColumn).setCellValue(product.getProductWidthUOM());
				String bundleDimAttr = configurationService.getConfiguration().getString("product.retail.bundleDimension.attribute");
				String bundleDimensions= getClassificationData(product.getClassifications(), bundleDimAttr);
				List<String> bundleDimension = splitDimension(bundleDimensions);
				//Bundle Gross Weight,Bundle Gross Weight UOM,Bundle GTIN
				List<String> bundleAttributes1 = getClassificationAttributes("product.retail.bundle.attributes1");
				if(null != bundleAttributes1) {
				for(int i=0;i<bundleAttributes1.size();i++) {
					startColumn = createClassificationRow(row,startColumn,product.getClassifications(),bundleAttributes1.get(i));	
				}
				}
				//Bundle Height
				if(!bundleDimension.isEmpty() && bundleDimension.size() >= 3)
				{
					row.createCell(++startColumn).setCellValue(bundleDimension.get(2));
				}
				else{
					row.createCell(++startColumn).setCellValue("");
				}
				//Bundle Height UOM
				List<String> bundleAttributes2 = getClassificationAttributes("product.retail.bundle.attributes2");
				if(null != bundleAttributes2) {
				for(int i=0;i<bundleAttributes2.size();i++) {
					startColumn = createClassificationRow(row,startColumn,product.getClassifications(),bundleAttributes2.get(i));	
				}
				}
				//Bundle Length
				if(!bundleDimension.isEmpty())
				{
				row.createCell(++startColumn).setCellValue(bundleDimension.get(0));
				}
				else{
					row.createCell(++startColumn).setCellValue("");
				}
				//Bundle Length UOM,Bundle Net Weight,Bundle Net Weight UOM,Bundle Volume,Bundle Volume UOM
				List<String> bundleAttributes3 = getClassificationAttributes("product.retail.bundle.attributes3");
				if(null != bundleAttributes3) {
				for(int i=0;i<bundleAttributes3.size();i++) {
					startColumn = createClassificationRow(row,startColumn,product.getClassifications(),bundleAttributes3.get(i));	
				}
				}
				//Bundle Width
				if(!bundleDimension.isEmpty() && bundleDimension.size() >= 2)
				{
				row.createCell(++startColumn).setCellValue(bundleDimension.get(1));
				}
				else{
					row.createCell(++startColumn).setCellValue("");
				}
				//Bundle Width UOM,Floor Ship 53 FT,Truck Load Qty (Floor),Truck Load Qty (Pallet),Pallet Ship 53 Ft,Sheet Count,Sheet Length,Sheet Width
				List<String> bundleAttributes4 = getClassificationAttributes("product.retail.bundle.attributes4");
				if(null != bundleAttributes4) {
				for(int i=0;i<bundleAttributes4.size();i++) {
					startColumn = createClassificationRow(row,startColumn,product.getClassifications(),bundleAttributes4.get(i));	
				}
				}
				//QSU Gross Volume CFT,QSU Gross Weight,QSU Hi,QSU Volume,QSU Net Weight,QSU Ti,QSU TiHi,QSU UPC
				List<String> qsuAttributes = getClassificationAttributes("product.retail.qsu.attributes");
				if(null != qsuAttributes) {
				for(int i=0;i<qsuAttributes.size();i++) {
					startColumn = createClassificationRow(row,startColumn,product.getClassifications(),qsuAttributes.get(i));	
				}
				}
				List<String> statAttributes = getClassificationAttributes("product.retail.stat.attributes");
				if(null != statAttributes) {
				for(int i=0;i<statAttributes.size();i++) {
					startColumn = createClassificationRow(row,startColumn,product.getClassifications(),statAttributes.get(i));	
				}
				}		
				row.createCell(++startColumn).setCellValue(product.getProductLengthUOM());
				row.createCell(++startColumn).setCellValue(product.getProductHeight());
				row.createCell(++startColumn).setCellValue(product.getProductLength());
				row.createCell(++startColumn).setCellValue(product.getProductHeightUOM());
				//Each Resaleable,Each Net Wgt,Each Gross Weight,Each Weight UOM
				row.createCell(++startColumn).setCellValue("");
				row.createCell(++startColumn).setCellValue(product.getEachNetWeight());
				row.createCell(++startColumn).setCellValue(product.getEachGrossWeight());
				row.createCell(++startColumn).setCellValue(product.getEachWeightUOM());
				//Each Width,Each Length,Each Height,Each Dimensions UOM,Each Volume,Each Volume UOM
				row.createCell(++startColumn).setCellValue(product.getEachProdWidth());
				row.createCell(++startColumn).setCellValue(product.getEachProdLength());
				row.createCell(++startColumn).setCellValue(product.getEachProdHeight());
				row.createCell(++startColumn).setCellValue(product.getEachDimensionsUOM());
				row.createCell(++startColumn).setCellValue(product.getEachvolume());
				row.createCell(++startColumn).setCellValue(product.getEachvolumeUOM());
				startColumn = createClassificationRow(row,startColumn,product.getClassifications(),"Capacity");
				row.createCell(++startColumn).setCellValue(product.getCaseWidth());
				row.createCell(++startColumn).setCellValue(product.getCaseLength());
				row.createCell(++startColumn).setCellValue(product.getCaseHeight());
				row.createCell(++startColumn).setCellValue(product.getCaseDimensionsUOM());
				row.createCell(++startColumn).setCellValue(product.getCaseVolume());
				//Case Cube UOM
				row.createCell(++startColumn).setCellValue("");
				row.createCell(++startColumn).setCellValue(product.getCaseNetWeight());
				row.createCell(++startColumn).setCellValue(product.getCaseGrossWeight());
				row.createCell(++startColumn).setCellValue(product.getCaseWeightUOM());
				startColumn = createClassificationRow(row,startColumn,product.getClassifications(),"TI-QTY/Layer");
				startColumn = createClassificationRow(row,startColumn,product.getClassifications(),"HI-Layers/Unit");
				row.createCell(++startColumn).setCellValue(product.getUnitQty());
				//Unit Floor Length,Unit Floor Width,Unit Floor Height,Unit Dimensions Uom
				row.createCell(++startColumn).setCellValue("");
				row.createCell(++startColumn).setCellValue("");
				row.createCell(++startColumn).setCellValue("");
				row.createCell(++startColumn).setCellValue("");
				row.createCell(++startColumn).setCellValue(product.getSellingstmt());
				row.createCell(++startColumn).setCellValue(product.getDescription());
				//Product Class,Start Availability Date,Freight Class
				row.createCell(++startColumn).setCellValue("");
				row.createCell(++startColumn).setCellValue("");
				row.createCell(++startColumn).setCellValue("");
				startColumn = createClassificationRow(row,startColumn,product.getClassifications(),"Ply");
				startColumn = createClassificationRow(row,startColumn,product.getClassifications(),"Roll Diameter");
				row.createCell(++startColumn).setCellValue(product.getPriceText());
			}
			
			public List<String> splitHeader(String header) {
				List<String> items = Arrays.asList(header.split("\\s*,\\s*"));
				return items;
			}
			
			public boolean ifRetailSoldTo() {
				final B2BUnitModel unit = sessionService.getAttribute(GpcommerceCoreConstants.SOLD_TO_ID);
				boolean soldToFlag = false;
				if(null != unit && unit.getUid().equalsIgnoreCase(configurationService.getConfiguration().getString(RETAIL_SOLD_TO)))
				{
					soldToFlag= true;
				}
				return soldToFlag;
			}
			
			public List<String> getClassificationAttributes(String propertyKey) {
				List<String> productAttributesList = null;
				
				final String attributes = configurationService.getConfiguration().getString(propertyKey);
				if(StringUtils.isNotBlank(attributes))
				{
					productAttributesList = Arrays.asList(StringUtils.split(attributes, GpcommerceCoreConstants.COMMA));
				}
				return productAttributesList;
			}
			
			public List<String> splitDimension(String dimension) {
				List<String> dimensions = new ArrayList<>();
				if(!dimension.isEmpty())
				{
					dimensions = Arrays.asList(StringUtils.split(dimension, GpcommerceCoreConstants.DIMENSION));
				}
				return dimensions;
			}
			/**
			 * Check if gpxpress email id
			 * @param email
			 * @return boolean
			 */
			public boolean valid(String email) 
		    { 
		        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+"[a-zA-Z0-9_+&*-]+)*@"+"(?:[a-zA-Z0-9-]+\\.)+[a-z"+"A-Z]{2,7}$"; 
		        Pattern pat = Pattern.compile(emailRegex); 
		        return pat.matcher(email).matches(); 
		    } 
			public BaseSiteService getBaseSiteService() {
				return baseSiteService;
			}
			@Required
			public void setBaseSiteService(BaseSiteService baseSiteService) {
				this.baseSiteService = baseSiteService;
			}
            @Required
			public void setConfigurationService(ConfigurationService configurationService) {
				this.configurationService = configurationService;
			}
            @Required
			public void setSessionService(SessionService sessionService) {
				this.sessionService = sessionService;
			}
}
