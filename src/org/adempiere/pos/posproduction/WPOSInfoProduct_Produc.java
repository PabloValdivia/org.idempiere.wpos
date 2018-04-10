/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * Copyright (C) 2003-2014 E.R.P. Consultores y Asociados, C.A.               *
 * All Rights Reserved.                                                       *
 * Contributor(s): Raul Muñoz www.erpcya.com					              *
 *****************************************************************************/

package org.adempiere.pos.posproduction;

import org.adempiere.pos.service.ProductInfo;
import org.adempiere.webui.component.Borderlayout;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.component.GridFactory;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.Panel;
import org.adempiere.webui.component.Row;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.util.ZKUpdateUtil;
import org.compiere.model.MPOSKey;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.North;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Image;

import java.math.BigDecimal;

/**
 * @author Mario Calderon, mario.calderon@westfalia-it.com, Systemhaus Westfalia, http://www.westfalia-it.com
 * @author Raul Muñoz, rmunoz@erpcya.com, ERPCYA http://www.erpcya.com
 * @author Yamel Senih, ysenih@erpcya.com, ERPCyA http://www.erpcya.com
 * @author victor.perez@e-evolution.com , http://www.e-evolution.com
 */
public class WPOSInfoProduct_Produc extends WPOSSubPanel_Produc {
	
	/**
	 * 
	 * *** Constructor ***
	 * @param posPanel
	 */
	public WPOSInfoProduct_Produc(WPOS_Produc posPanel) {
		super(posPanel);
		
	}
	
	private Panel 		parameterPanel;
	/**	Image Product		*/
	private Panel 		buttonImage;
	/**	Product Code		*/
	private Label 		labelValue;
	/**	Product Name		*/
	private Label 		labelName;
	/**	Product Description	*/
	private Label 		labelDescription;
	/**	Product UOM Symbol	*/
	private Label 		labelUOMSymbol;
	/**	Product Category	*/
	private Label 		labelProductCategory;
	/** Grid Panel 			*/
	private Grid 		infoProductLayout;
	private Grid 		labelLayout;
//	private Panel 		buttonPanel;
	/**
	 * 
	 */
	private static final long serialVersionUID = -175459707049618428L;

	@Override
	public void onEvent(Event arg0) throws Exception {
		
	}

	@Override
	protected void init() {
		parameterPanel = new Panel();
		Groupbox groupPanel = new Groupbox();
		infoProductLayout = GridFactory.newGridLayout();

		Caption v_TitleBorder = new Caption(Msg.getMsg(Env.getCtx(), "InfoProduct"));
		groupPanel.appendChild(v_TitleBorder);
		groupPanel.appendChild(infoProductLayout);
		
		labelLayout = GridFactory.newGridLayout();
		
//		buttonPanel = new Panel();

//		buttonPanel.appendChild(labelLayout);
		parameterPanel.appendChild(groupPanel);
		
//		buttonPanel.setStyle("border: none; width:99%;moz-box-shadow: 0 0 0px #888;-webkit-box-shadow: 0 0 0px #888;box-shadow: 0 0 0px #888;");
		labelLayout.setStyle("border: none; width:100%;moz-box-shadow: 0 0 0px #888;-webkit-box-shadow: 0 0 0px #888;box-shadow: 0 0 0px #888;");
		infoProductLayout.setStyle("border: none; width:100%; moz-box-shadow: 0 0 0px #888;-webkit-box-shadow: 0 0 0px #888;box-shadow: 0 0 0px #888;");
		parameterPanel.setStyle("border: none; width:99%;");
		Rows rows = null;
		Row  row = null;
		rows = infoProductLayout.newRows();
		row = rows.newRow();
		
		//	For Image
		buttonImage = new Panel();
		row.appendChild(buttonImage);
//		buttonImage.setWidth("138px");
//		buttonImage.setHeight("130px");
		ZKUpdateUtil.setWidth(buttonImage, "300px");
		ZKUpdateUtil.setHeight(buttonImage, "200px");
		
//		row.appendChild(buttonPanel);
		row.appendChild(labelLayout);
		rows = labelLayout.newRows();
		row = rows.newRow();
		//	For Value
		labelValue = new Label ();
		labelValue.setStyle(WPOS_Produc.FONTSIZEMEDIUM+" font-weight:bold");
		//	Add
		row.appendChild(labelValue);
				
		row = rows.newRow();
		//	For Name
		labelName = new Label ();
		labelName.setStyle(WPOS_Produc.FONTSIZEMEDIUM+" font-weight:bold");
		//	Add
		row.appendChild(labelName);
		
		row = rows.newRow();
		//  For UOM
		labelUOMSymbol = new Label (Msg.getElement(Env.getCtx(), "C_UOM_ID"));
		labelUOMSymbol.setStyle(WPOS_Produc.FONTSIZEMEDIUM+" font-weight:bold");
		//	Add
		row.appendChild(labelUOMSymbol);
		
		row = rows.newRow();
		//	For Category
		labelProductCategory = new Label(Msg.getElement(Env.getCtx(), "M_Product_Category_ID"));
		labelProductCategory.setStyle(WPOS_Produc.FONTSIZEMEDIUM+" font-weight:bold");
		//	Add
		row.appendChild(labelProductCategory);
				
		row = rows.newRow();
		//	For Description
		labelDescription = new Label ();

		labelDescription.setHeight("19px");

		labelDescription.setClass("label-description");
		//	Add
		row.appendChild(labelDescription);
		initialValue();
	}
	
	/**
	 * Initial value
	 * @param key
	 * @return void
	 */
	public void initialValue() {
		labelDescription.setText(Msg.getElement(Env.getCtx(), "Description"));
		labelName.setText(Msg.getElement(Env.getCtx(), "ProductName"));
		labelValue.setText(Msg.getElement(Env.getCtx(), "ProductValue"));
		labelUOMSymbol.setText(Msg.getElement(Env.getCtx(), "C_UOM_ID"));
		labelProductCategory.setText(Msg.getElement(Env.getCtx(), "M_Product_Category_ID"));
		buttonImage.getChildren().clear();
	}
	
	/**
	 * Get Panel 
	 * @return Panel
	 */
	public Panel getPanel(){
		return parameterPanel;
	}

	/**
	 * setValuesFromProduct
	 * @param productId
	 * @param imageId
     */
	public void setValuesFromProduct(int productId, BigDecimal quantity , int imageId) {
		if(productId <= 0){
			initialValue();
			return;
		}
		
		//	Refresh Values
		ProductInfo productInfo = new ProductInfo(productId, quantity ,  imageId , 0 , 0);
		labelValue.setText(productInfo.value);
		labelName.setText(productInfo.name);
		labelUOMSymbol.setText(productInfo.uomSymbol);
		labelProductCategory.setText(productInfo.productCategoryName);
		labelDescription.setText(productInfo.description);
		posPanel.updateProductPlaceholder(productInfo.name);
		if(productInfo.imageData != null) {
			North nt = new North();
			Borderlayout mainLayout = new Borderlayout();
			AImage img = null;
			byte[] data = productInfo.imageData;
			if (data != null && data.length > 0) {
				try {
					img = new AImage(null, data);				
				} catch (Exception e) {		
				}
			}
			Image bImg = new Image();
			bImg.setContent(img);
			bImg.setWidth("100%");
			bImg.setHeight("200px");
			
			nt.appendChild(bImg);
		
//		buttonImage.setClass("z-button");
		
		mainLayout.appendChild(nt);
		mainLayout.setStyle("background-color: transparent");
		nt.setStyle("background-color: transparent");
		buttonImage.getChildren().clear();
		buttonImage.appendChild(mainLayout);
		buttonImage.invalidate();
		infoProductLayout.invalidate();
		labelLayout.invalidate();
//		buttonPanel.invalidate();
		} else {
			buttonImage.getChildren().clear();
			buttonImage.invalidate();
		}
	}

	/**
	 * Refresh Product from Key
	 * @param key
	 * @return void
	 */
	public void refreshProduct(MPOSKey key ,BigDecimal quantity) {
		if(key == null) {
			initialValue();
			return;
		}
		setValuesFromProduct(key.getM_Product_ID() , quantity , key.getAD_Image_ID());
	}

	/**
	 * Refresh from product
	 * @param productId
	 * @return void
	 */
	public void refreshProduct(int productId , BigDecimal quantity) {
		int imageId = posPanel.getProductImageId(productId, posPanel.getC_POSKeyLayout_ID());
		setValuesFromProduct(productId, quantity , imageId );
	}
	
	/**
	 * Reset Values of Info Product
	 * @return void
	 */
	public void resetValues() {
		final String NO_TEXT = "--";
		labelValue.setText(NO_TEXT);
		labelName.setText(NO_TEXT);
		labelUOMSymbol.setText(NO_TEXT);
		labelProductCategory.setText(NO_TEXT);
		labelDescription.setText(NO_TEXT);
		buttonImage.getChildren().clear();
		buttonImage.invalidate();
	}

	public String getUOMSymbol()
	{
		return  labelUOMSymbol.getValue();
	}
}