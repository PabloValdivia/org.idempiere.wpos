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
 * Copyright (C) 2015 Systemhaus Westfalia                                    *
 * All Rights Reserved.                                                       *
 * Contributor(s): Raul Muñoz www.erpcya.com					              *
 * Contributor(s): Mario Calderon, www.westfalia-it.com  		              *
 *****************************************************************************/

package org.adempiere.pos;

import java.awt.Color;
import java.awt.Event;
import java.awt.event.KeyEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

import javax.swing.KeyStroke;

import org.adempiere.pos.search.WPosQuery;
import org.adempiere.pos.search.WQueryBPartner;
import org.adempiere.pos.search.WQueryProduct;
import org.adempiere.pos.search.WQueryTicket;
import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.component.Borderlayout;
import org.adempiere.webui.component.Button;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.component.GridFactory;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.ListModelTable;
import org.adempiere.webui.component.ListboxFactory;
import org.adempiere.webui.component.Panel;
import org.adempiere.webui.component.Row;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.component.Textbox;
import org.adempiere.webui.component.WListbox;
import org.adempiere.webui.event.TableValueChangeEvent;
import org.adempiere.webui.event.TableValueChangeListener;
import org.adempiere.webui.event.WTableModelEvent;
import org.adempiere.webui.event.WTableModelListener;
import org.adempiere.webui.window.FDialog;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.IDColumn;
import org.compiere.model.MBPartner;
import org.compiere.model.MBPartnerInfo;
import org.compiere.model.MCurrency;
import org.compiere.model.MImage;
import org.compiere.model.MInvoice;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MPOSKey;
import org.compiere.model.MPOSKeyLayout;
import org.compiere.model.MPriceList;
import org.compiere.model.MPriceListVersion;
import org.compiere.model.MProduct;
import org.compiere.model.MSequence;
import org.compiere.model.MSysConfig;
import org.compiere.model.MUser;
import org.compiere.model.MWarehousePrice;
import org.compiere.model.PO;
import org.compiere.print.MPrintColor;
import org.compiere.print.ReportCtl;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkex.zul.Center;
import org.zkoss.zkex.zul.East;
import org.zkoss.zkex.zul.North;
import org.zkoss.zkex.zul.South;
import org.zkoss.zkex.zul.West;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Space;


/**
 *	Customer Sub Panel
 *	
 * @author Raul Muñoz 20/03/2015 
 */
public class WSubOrder extends WPosSubPanel 
	implements EventListener, WTableModelListener, TableValueChangeListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5895558315889871887L;

	/**
	 * 	Constructor
	 *	@param posPanel POS Panel
	 */
	public WSubOrder (WPOS posPanel)
	{
		super (posPanel);
	}	//	PosSubCustomer
	
	
	private Button 		f_history;
	private	Label		f_name;
	private Button 		f_bNew;
	private Button 		f_cashPayment;

	private Button 		f_process;
	private Button 		f_print;
	private Label	 	f_DocumentNo;
	private Button 		f_logout;
	private Button 		f_cancel;
	private Label	 	f_net;
	private Label	 	f_tax;
	private Label	 	f_total;
	private Label 		f_RepName;
	private Doublebox	f_discount;
	private Button 			f_Up;
	private Button 			f_Down;
	private Button 			f_Next;
	private Button 			f_Back;
	
	/**	The Business Partner		*/
	private MBPartner	m_bpartner;
	private Textbox f_currency = new Textbox();
	private Button f_bCreditSale;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(WSubOrder.class);
	
	
	private Button 			f_delete;
	//
	private Double	 		f_price;
	private Double	 		f_quantity;
	private WPosTextField	f_name1;
	private Button			f_bBPartner;
	private Button			f_bSearch;
	private int orderLineId = 0;
	private int currentLayout;
	/** The Table					*/
	private WListbox		m_table;
	/** The Query SQL				*/
	private String			m_sql;
	/** Status Panel */
	private boolean status;
	private int BPartnerStd;
	private Panel all_SubCard;
	private Panel popular_SubCard;
	/**	Table Column Layout Info			*/
	private static ColumnInfo[] s_layout = new ColumnInfo[] 
	{
		new ColumnInfo(" ", "C_OrderLine_ID", IDColumn.class), 
		new ColumnInfo(Msg.translate(Env.getCtx(), "Name"), "p_Name", String.class),
		new ColumnInfo(Msg.translate(Env.getCtx(), "Qty"), "QtyOrdered", Double.class,false,true,null),
		new ColumnInfo(Msg.translate(Env.getCtx(), "C_UOM_ID"), "UOM_name", String.class),
		new ColumnInfo(Msg.translate(Env.getCtx(), "PriceActual"), "PriceActual", BigDecimal.class,false,true,null), 
		new ColumnInfo(Msg.translate(Env.getCtx(), "LineNetAmt"), "LineNetAmt", BigDecimal.class), 
		new ColumnInfo(Msg.translate(Env.getCtx(), "C_Tax_ID"), "TaxIndicator", String.class, true, true, null), 
		new ColumnInfo(Msg.translate(Env.getCtx(), "GrandTotal"), "GrandTotal", BigDecimal.class,  true, true, null), 
	};
	/**	From Clause							*/
	private static String s_sqlFrom ;
	/** Where Clause						*/
	private static String s_sqlWhere; 
	/** Map of map of keys */
	private HashMap<Integer, HashMap<Integer, MPOSKey>> keymap;
	private Panel button;

	private int keyLayoutId;
	
	/**	The Product					*/
	private MProduct		m_product = null;

	/**	Price List Version to use	*/
	private int			m_M_PriceList_Version_ID = 0;
	/** Warehouse					*/
	private int 			m_M_Warehouse_ID;
	private ArrayList<Integer> orderList;
	private int recordposition;
	private int cont; 
	private final String POS_ALTERNATIVE_DOCTYPE_ENABLED = "POS_ALTERNATIVE_DOCTYPE_ENABLED";  // System configurator entry
	private final String NO_ALTERNATIVE_POS_DOCTYPE      = "N";
	private final boolean isAlternativeDocTypeEnabled    = MSysConfig.getValue(POS_ALTERNATIVE_DOCTYPE_ENABLED, 
			NO_ALTERNATIVE_POS_DOCTYPE, Env.getAD_Client_ID(p_ctx)).compareToIgnoreCase(NO_ALTERNATIVE_POS_DOCTYPE)==0?false:true;
	
	private final String BG_GRADIENT = "";
	private final String ACTION_BPARTNER    = "BPartner";
	private final String ACTION_LOGOUT      = "Cancel";
	private final String ACTION_CANCEL      = "End";
	private final String ACTION_CREDITSALE  = "Credit Sale";
	private final String ACTION_HISTORY     = "History";
	private final String ACTION_NEW         = "New";
	private final String ACTION_PAYMENT     = "Payment";

	/**
	 * 	Initialize
	 */
	public void init()
	{
		//	Content
		this.setHeight("100%");
		this.setWidth("99%");
		status = false;
		cont  = 0;
		boolean isModifiyPrice = p_pos.isModifyPrice();
		keymap = new HashMap<Integer, HashMap<Integer,MPOSKey>>();
		listOrder();
		recordposition = orderList.size()-1;
		
		s_sqlFrom = "POS_OrderLine_v";
		/** Where Clause						*/
		s_sqlWhere = "C_Order_ID=? AND LineNetAmt <> 0";
		
		Panel parameterPanel = new Panel();
		Borderlayout detailPanel = new Borderlayout();
		Grid parameterLayout = GridFactory.newGridLayout();
		Panel productPanel = new Panel();
		Borderlayout fullPanel = new Borderlayout();
		Grid productLayout = GridFactory.newGridLayout();
		Grid parameterLayout3 = GridFactory.newGridLayout();
		Rows rows = null;
		Row row = null;

		East east = new East();
		east.setStyle("border: none; width:40%");
		east.setAutoscroll(true);
		appendChild(east);
		productPanel.appendChild(productLayout);
		productLayout.setWidth("100%");
		rows = productLayout.newRows();
		row = rows.newRow();
		
		int C_POSKeyLayout_ID = p_pos.getC_POSKeyLayout_ID();
		if (C_POSKeyLayout_ID == 0)
			return;
		currentLayout = C_POSKeyLayout_ID;
		east.appendChild(
				createPanel(C_POSKeyLayout_ID));
		
		West west = new West();
		west.setStyle("border: none;");
		appendChild(west);
		west.appendChild(fullPanel);
		fullPanel.setWidth("100%");
		fullPanel.setHeight("100%");
		North north = new North();
		north.setStyle("border: none; width:60%");
		north.setZindex(0);
		fullPanel.appendChild(north);
		parameterPanel.appendChild(parameterLayout);
		parameterLayout.setWidth("60%");
		north.appendChild(parameterPanel);
		rows = parameterLayout.newRows();
		row = rows.newRow();
		
		setStyle("border: none");
		
		m_table = ListboxFactory.newDataTable();
		m_sql = m_table.prepareTable(s_layout, s_sqlFrom, 
			s_sqlWhere, false, "POS_OrderLine_v");
		m_table.autoSize();

		m_table.getModel().addTableModelListener(this);
		
		m_table.setColumnClass(4, BigDecimal.class, !isModifiyPrice);
		m_table.setInnerHeight("20%");
		Center center = new Center();
		center.setStyle("border: none; width:400px");
		appendChild(center);
		center.appendChild(detailPanel);
		north = new North();
		north.setStyle("border: none");
		detailPanel.setHeight("40%");
		detailPanel.setWidth("50%");
		detailPanel.appendChild(north);
		
		keyLayoutId=p_pos.getOSNP_KeyLayout_ID();
		setQty(Env.ONE);
		
		setPrice(Env.ZERO);

		center = new Center();
		detailPanel.appendChild(center);
		center.appendChild(m_table);
		m_table.setWidth("100%");
		m_table.setHeight("99%");
		m_table.addActionListener(this);
		center.setStyle("border: none");
		m_table.loadTable(new PO[0]);
		
		north.appendChild(parameterLayout3);
		parameterLayout3.setWidth("100%");
		parameterLayout3.setHeight("100%");
		rows = parameterLayout3.newRows();
		parameterLayout3.setStyle("border:none");
		row = rows.newRow();
		row.setHeight("60px");

		row.appendChild(new Space());
		// NEW
		f_bNew = createButtonAction(ACTION_NEW, KeyStroke.getKeyStroke(KeyEvent.VK_F2, Event.F2));
		f_bNew.addActionListener(this);
		row.appendChild(f_bNew);

		// BPartner Search
		f_bBPartner = createButtonAction(ACTION_BPARTNER, p_pos.getOSK_KeyLayout_ID());
		f_bBPartner.addActionListener(this);
		f_bBPartner.setTooltiptext(Msg.translate(p_ctx, "IsCustomer"));
		row.appendChild(f_bBPartner);
				
		// EDIT
		f_bCreditSale = createButtonAction(ACTION_CREDITSALE, null);
		f_bCreditSale.addActionListener(this);
		row.appendChild(f_bCreditSale);
		f_bCreditSale.setEnabled(false);
				
		// HISTORY
		f_history = createButtonAction(ACTION_HISTORY, null);
		f_history.addActionListener(this);
		row.appendChild(f_history); 

		f_Back = createButtonAction("Parent", KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0));
		f_Back.setTooltiptext(Msg.translate(p_ctx, "Previous"));
		row.appendChild (f_Back);
		f_Next = createButtonAction("Detail", KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0));
		f_Next.setTooltiptext(Msg.translate(p_ctx, "Next"));
		row.appendChild (f_Next);
		
		// PAYMENT
		f_cashPayment = createButtonAction(ACTION_PAYMENT, null);
		f_cashPayment.addActionListener(this);
		row.appendChild(f_cashPayment); 
		f_cashPayment.setEnabled(false);

		// Cancel
		f_cancel = createButtonAction (ACTION_CANCEL, null);
		f_cancel.addActionListener(this);
		f_cancel.setTooltiptext(Msg.translate(p_ctx, "POS.IsCancel"));
		row.appendChild (f_cancel);
		f_cancel.setEnabled(false);
		
		// LOGOUT
		f_logout = createButtonAction (ACTION_LOGOUT, null);
		f_logout.addActionListener(this);
		f_logout.setTooltiptext(Msg.translate(p_ctx, "End"));
		row.appendChild (f_logout);
		row.appendChild(new Space());
		
		row = rows.newRow();
		row.setSpans("3,5");
		row.setHeight("25px");
		// BP
		Label bpartner = new Label(Msg.translate(Env.getCtx(), "IsCustomer")+":");
		row.appendChild (bpartner.rightAlign());
		bpartner.setStyle("Font-size:medium; font-weight:700");
		
		f_name = new Label();
		f_name.setStyle("Font-size:medium");
		f_name.setWidth("100%");
		row.appendChild  (f_name);

	}	//	init
	
	public Panel createButton(int C_POSKeyLayout_ID){
		if ( keymap.containsKey(C_POSKeyLayout_ID) ) {
			return null;
		}
		Panel card = new Panel();
		card.setWidth("100%");
		MPOSKeyLayout keyLayout = MPOSKeyLayout.get(Env.getCtx(), C_POSKeyLayout_ID);
		Color stdColor = Color.lightGray;
		if (keyLayout.getAD_PrintColor_ID() != 0)
		{
			MPrintColor color = MPrintColor.get(Env.getCtx(), keyLayout.getAD_PrintColor_ID());
			stdColor = color.getColor();
		}
		if (keyLayout.get_ID() == 0)
			return null;
		MPOSKey[] keys = keyLayout.getKeys(false);
		
		HashMap<Integer, MPOSKey> map = new HashMap<Integer, MPOSKey>(keys.length);

		keymap.put(C_POSKeyLayout_ID, map);
		
		int COLUMNS = 3;	//	Min Columns
		int ROWS = 3;		//	Min Rows
		int noKeys = keys.length;
		int cols = keyLayout.getColumns();
		if ( cols == 0 )
			cols = COLUMNS;
		int buttons = 0;
		log.fine( "PosSubFunctionKeys.init - NoKeys=" + noKeys 
			+ ", Cols=" + cols);
		//	Content
		Panel content = new Panel ();
				
		for (MPOSKey key :  keys)
		{
			if(!key.getName().equals("")){
			map.put(key.getC_POSKey_ID(), key);
			Color keyColor = stdColor;
			
			if (key.getAD_PrintColor_ID() != 0)	{
				MPrintColor color = MPrintColor.get(Env.getCtx(), key.getAD_PrintColor_ID());
				keyColor = color.getColor();
			}
			
			log.fine( "#" + map.size() + " - " + keyColor); 
			button = new Panel();
			Label label = new Label(key.getName());
			
			North nt = new North();
			South st = new South();
			Borderlayout mainLayout = new Borderlayout();
			if ( key.getAD_Image_ID() != 0 )
			{
				MImage m_mImage = MImage.get(Env.getCtx(), key.getAD_Image_ID());
				AImage img = null;
				byte[] data = m_mImage.getData();
				if (data != null && data.length > 0) {
					try {
						img = new AImage(null, data);				
					} catch (Exception e) {		
					}
				}
				Image bImg = new Image();
				bImg.setContent(img);
				bImg.setWidth("50%");
				bImg.setHeight("50px");
				nt.appendChild(bImg);
			}
			label.setStyle("word-wrap: break-word; white-space: pre-line;margin: 25px 0px 0px 0px; top:20px; font-size:10pt; font-weight: bold;color: #FFF;");
			label.setHeight("100%");
			button.setHeight("70px");
			st.appendChild(label);
			button.setClass("z-button");
			button.setStyle("float:left; white-space: pre-line;text-align:center; margin:0.4% 1%; Background-color:rgb("+keyColor.getRed()+","+keyColor.getGreen()+","+keyColor.getBlue()+"); border: 2px outset #CCC; "
					+ "background: -moz-linear-gradient(top, rgba(247,247,247,1) 0%, rgba(255,255,255,0.93) 7%, rgba(186,186,186,0.25) 15%, rgba("+keyColor.getRed()+","+keyColor.getGreen()+","+keyColor.getBlue()+",1) 100%);"
					+ "background: -webkit-gradient(left top, left bottom, color-stop(0%, rgba(247,247,247,1)), color-stop(7%, rgba(255,255,255,0.93)), color-stop(15%, rgba(186,186,186,0.25)), color-stop(100%, rgba("+keyColor.getRed()+","+keyColor.getGreen()+","+keyColor.getBlue()+",1)));"
					+ "background: -webkit-linear-gradient(top, rgba(247,247,247,1) 0%, rgba(255,255,255,0.93) 7%, rgba(186,186,186,0.25) 15%, rgba("+keyColor.getRed()+","+keyColor.getGreen()+","+keyColor.getBlue()+",1) 100%);");
			
			mainLayout.appendChild(nt);
			mainLayout.appendChild(st);
			mainLayout.setStyle("background-color: transparent");
			nt.setStyle("background-color: transparent");
			st.setStyle("clear: both; background-color: #333; opacity: 0.6;");
			st.setZindex(99);
			button.appendChild(mainLayout);
			
			button.setId(""+key.getC_POSKey_ID());
			button.addEventListener("onClick", this);

			int size = 1;
			if ( key.getSpanX() > 1 )
			{
				size = key.getSpanX();
				button.setWidth("96%");
			}
			else 
				button.setWidth(88/cols+"%");
			if ( key.getSpanY() > 1 )
			{
				size = size*key.getSpanY();
			}
			buttons = buttons + size;
			content.appendChild(button);
		}
		}
		int rows = Math.max ((buttons / cols), ROWS);
		if ( buttons % cols > 0 )
			rows = rows + 1;


		
		card.appendChild(content);
		
		return card;
	}
	public Panel createPanel(int C_POSKeyLayout_ID){
		Panel card = new Panel();
		card.setWidth("100%");
		MPOSKeyLayout keyLayout = MPOSKeyLayout.get(Env.getCtx(), C_POSKeyLayout_ID);
		North north = new North();
		Grid parameterLayout3 = GridFactory.newGridLayout();
		Rows rows = null;
		Row row = null;		
		north.appendChild(parameterLayout3);
		parameterLayout3.setWidth("500px");
		parameterLayout3.setHeight("100%");
		rows = parameterLayout3.newRows();
		parameterLayout3.setStyle("border:none");
		
		//
		row = rows.newRow();
		row.setHeight("10px");
		// DOC NO
		Label docNo = new Label(Msg.getMsg(Env.getCtx(),"DocumentNo")+":");
		row.appendChild (docNo.rightAlign());

		docNo.setStyle("Font-size:medium; font-weight:700");
		f_DocumentNo = new Label();
		f_DocumentNo.setStyle("Font-size:medium");
		row.appendChild(f_DocumentNo);

		Label lNet = new Label (Msg.translate(Env.getCtx(), "SubTotal")+":");
		lNet.setStyle("Font-size:medium; font-weight:700");
		row.appendChild(lNet.rightAlign());
		f_net = new Label(String.valueOf(DisplayType.Amount));
		f_net.setStyle("Font-size:medium; width:200px");
		row.appendChild(f_net.rightAlign());
		
		f_net.setText("0.00");
		
		row = rows.newRow();
		row.setHeight("30px");
		// SALES REP
		Label l_SalesRep = new Label(Msg.translate(Env.getCtx(), "POS.SalesRep_ID")+":");
		row.appendChild(l_SalesRep.rightAlign());
		l_SalesRep.setStyle("Font-size:medium; font-weight:700");
		MUser salesRep = new MUser(p_ctx, Env.getAD_User_ID(p_ctx), null);
		f_RepName = new Label(salesRep.getName());
		f_RepName.setStyle("Font-size:medium");
		row.appendChild (f_RepName);
		
		Label lTax = new Label (Msg.translate(Env.getCtx(), "C_Tax_ID")+":");
		lTax.setStyle("Font-size:medium; font-weight:700");
		row.appendChild(lTax.rightAlign());
		f_tax = new Label(String.valueOf(DisplayType.Amount));
		f_tax.setStyle("Font-size:medium");
		row.appendChild(f_tax.rightAlign());
		f_tax.setText(Env.ZERO.toString());
		
		row = rows.newRow();
		row.appendChild(new Space());		
		row.appendChild(new Space());		
		row.appendChild(new Space());
		row.setHeight("5px");
		Label line = new Label ("____________________");
		row.appendChild(line.rightAlign());
		
		row = rows.newRow();
		row.appendChild(new Space());		
		row.appendChild(new Space());
		Label lTotal = new Label (Msg.translate(Env.getCtx(), "GrandTotal")+":");
		lTotal.setStyle("Font-size:medium; font-weight:700");
		row.appendChild(lTotal.rightAlign());
		f_total = new Label(String.valueOf(DisplayType.Amount));
		row.appendChild(f_total.rightAlign());
		f_total.setText(Env.ZERO.toString());
		f_total.setStyle("Font-size:medium");
		row.setWidth("25%");
		card.appendChild(parameterLayout3);
		f_name1 = new WPosTextField(v_POSPanel, p_pos.getOSK_KeyLayout_ID());
		f_name1.setWidth("80%");
		f_name1.setHeight("35px");
		f_name1.setName("Name");
		f_name1.setReadonly(true);
		f_name1.addEventListener("onFocus", this);
		

		Label productLabel = new Label(Msg.translate(Env.getCtx(), "M_Product_ID")+":");
		productLabel.setStyle("Font-size:medium; font-weight:700");
		card.appendChild(productLabel);

		card.appendChild(f_name1);

		if(popular_SubCard==null) {
			popular_SubCard = createButton(C_POSKeyLayout_ID);
			card.appendChild(popular_SubCard);
		}
		if (keyLayout.get_ID() == 0)
			return null;
		MPOSKey[] keys = keyLayout.getKeys(false);
		
		//	Content
		for (MPOSKey key :  keys)
		{
			if ( key.getSubKeyLayout_ID() > 0 )
			{
				if(all_SubCard == null){
					all_SubCard = createButton(key.getSubKeyLayout_ID());
				}
				if ( all_SubCard != null  ){
					if(status==false) {
						card.appendChild(all_SubCard);
						all_SubCard.setVisible(status);
						all_SubCard.setContext(""+key.getC_POSKey_ID());
						status=true;
					}
				}
					card.appendChild(all_SubCard);
			}
		}
		return card;
	}
	/**
	 * 	Dispose - Free Resources
	 */
	public void dispose()
	{
		f_name = null;
		super.dispose();
	}	//	dispose

	/**
	 * 
	 */
	private void printOrder() {
		{
			if (isOrderFullyPaid())
			{
				updateOrder();
				printTicket();
//				openCashDrawer();
			}
		}
	}

	/**
	 * 
	 */
	private void payOrder() {

		//Check if order is completed, if so, print and open drawer, create an empty order and set cashGiven to zero
		if( v_POSPanel.getM_Order() == null ) {
				FDialog.warn(0, Msg.getMsg(p_ctx, "You must create an Order first"));
				return;
		}
		else if ( WPosPayment.pay(v_POSPanel, this) ) {
				printTicket();
				v_POSPanel.setOrder(0);
				updateOrder();
				updateTable(null);
		}
	}
	
	/**
	 * Execute order prepayment
	 * If order is not processed, process it first.
	 * If it is successful, proceed to pay and print ticket
	 */
	private void prePayOrder() {
//		//Check if order is completed, if so, print and open drawer, create an empty order and set cashGiven to zero
//		if( m_order == null) {		
//			FDialog.warn(0, Msg.getMsg(p_ctx, "You must create an Order first"));
//		}
//		else
//		{
//			if ( WPosPrePayment.pay(v_POSPanel, this) )
//			{
//				v_POSPanel.setOrder(0);
//			}
//		}	
	}  // prePayOrder
	
	
	
	
	/**
	 * 	Find/Set BPartner
	 */
	private void findBPartner()
	{
		
		String query = f_name.getValue();
		
		if (query == null || query.length() == 0)
			return;
		
		// unchanged
		if ( m_bpartner != null && m_bpartner.getName().equals(query))
			return;
		
		query = query.toUpperCase();
		//	Test Number
		boolean allNumber = true;
		boolean noNumber = true;
		char[] qq = query.toCharArray();
		for (int i = 0; i < qq.length; i++)
		{
			if (Character.isDigit(qq[i]))
			{
				noNumber = false;
				break;
			}
		}
		try
		{
			Integer.parseInt(query);
		}
		catch (Exception e)
		{
			allNumber = false;
		}
		String Value = query;
		String Name = (allNumber ? null : query);
		String EMail = (query.indexOf('@') != -1 ? query : null); 
		String Phone = (noNumber ? null : query);
		String City = null;
		//
		//TODO: contact have been remove from rv_bpartner
		MBPartnerInfo[] results = MBPartnerInfo.find(p_ctx, Value, Name, 
			/*Contact, */null, EMail, Phone, City);
		
		//	Set Result
		if (results.length == 0)
		{
			setC_BPartner_ID(0);
		}
		else if (results.length == 1)
		{
			setC_BPartner_ID(results[0].getC_BPartner_ID());
			f_name.setText(results[0].getName());
			f_bBPartner.setImage("images/BPartner16.png");
		}
		else	//	more than one
		{
			WQueryBPartner qt = new WQueryBPartner(v_POSPanel);
			qt.setResults (results);
			qt.setVisible(true);
		}
	}	//	findBPartner
	
	
	/**************************************************************************
	 * 	Set BPartner
	 *	@param C_BPartner_ID id
	 */
	public void setC_BPartner_ID (int C_BPartner_ID)
	{
//		log.fine( "PosSubCustomer.setC_BPartner_ID=" + C_BPartner_ID);
//		if (C_BPartner_ID == 0){
//			m_bpartner = null;
//			
//		} else {
//			m_bpartner = MBPartner.get(p_ctx, C_BPartner_ID);
//		}
//		//	Set Info
//		if (m_bpartner != null) {
//			f_name.setText(m_bpartner.getName());
//		} else {
//			f_name.setText(null);
//		}
//		//	Sets Currency
//		m_M_PriceList_Version_ID = 0;
//		getM_PriceList_Version_ID();
		//fillCombos();
		v_POSPanel.setC_BPartner_ID(C_BPartner_ID); //added by ConSerTi to update the client in the request
	}	//	setC_BPartner_ID

	/**
	 * 	Get BPartner
	 *	@return C_BPartner_ID
	 */
	public int getC_BPartner_ID ()
	{
		if (m_bpartner != null)
			return m_bpartner.getC_BPartner_ID();
		return 0;
	}	//	getC_BPartner_ID

	/**
	 * 	Get BPartner
	 *	@return BPartner
	 */
	public MBPartner getBPartner ()
	{
		return m_bpartner;
	}	//	getBPartner
	
	/**
	 * 	Get M_PriceList_Version_ID.
	 * 	Set Currency
	 *	@return plv
	 */
	public int getM_PriceList_Version_ID()
	{
		if (m_M_PriceList_Version_ID == 0)
		{
			int M_PriceList_ID = p_pos.getM_PriceList_ID();
			if (m_bpartner != null && m_bpartner.getM_PriceList_ID() != 0)
				M_PriceList_ID = m_bpartner.getM_PriceList_ID();
			//
			MPriceList pl = MPriceList.get(p_ctx, M_PriceList_ID, null);
			setCurrency(MCurrency.getISO_Code(p_ctx, pl.getC_Currency_ID()));

			//
			MPriceListVersion plv = pl.getPriceListVersion (v_POSPanel.getToday());
			if (plv != null && plv.getM_PriceList_Version_ID() != 0)
				m_M_PriceList_Version_ID = plv.getM_PriceList_Version_ID();
		}
		return m_M_PriceList_Version_ID;
	}	//	getM_PriceList_Version_ID
	

	/***************************************************************************
	 * Set Currency
	 * 
	 * @param currency
	 *            currency
	 */
	public void setCurrency(String currency) {
		if (currency == null)
			f_currency.setText("---");
		else
			f_currency.setText(currency);
	} //	setCurrency
	
	/**
	 * 	Print Ticket
	 *  @author Raul Muñoz raulmunozn@gmail.com 
	 */
	public void printTicket()
	{
		if ( v_POSPanel.getM_Order()  == null )
			return;
		
		MOrder order = v_POSPanel.getM_Order();
		//int windowNo = v_POSPanel.getWindowNo();
		//Properties m_ctx = v_POSPanel.getPropiedades();
		
		if (order != null)
		{
			try 
			{
				//print standard document
				Boolean print = true;
				if (p_pos.getAD_Sequence_ID() != 0)
				{
					MSequence seq = new MSequence(Env.getCtx(), p_pos.getAD_Sequence_ID(), order.get_TrxName());
					String docno = seq.getPrefix() + seq.getCurrentNext();
					String q = "Confirmar el número consecutivo "  + docno;
					if (FDialog.ask(0, null, q))						
					{
						order.setPOReference(docno);
						order.saveEx();
						ReportCtl.startDocumentPrint(0, order.getC_Order_ID(), false);
						int next = seq.getCurrentNext() + seq.getIncrementNo();
						seq.setCurrentNext(next);
						seq.saveEx();
					}
				}
				else
					ReportCtl.startDocumentPrint(0, order.getC_Order_ID(), false);				
			}
			catch (Exception e) 
			{
				log.severe("PrintTicket - Error Printing Ticket");
			}
		}	  
	}
	
	/**
	 * Is order fully pay 
	 * @author Raul Muñoz 
	 */
	public boolean isOrderFullyPaid()
	{
		/*TODO
		BigDecimal given = new BigDecimal(f_cashGiven.getValue().toString());
		boolean paid = false;
		if (v_POSPanel != null && v_POSPanel.f_curLine != null)
		{
			MOrder order = v_POSPanel.f_curLine.getOrder();
			BigDecimal total = new BigDecimal(0);
			if (order != null)
				total = order.getGrandTotal();
			paid = given.doubleValue() >= total.doubleValue();
		}
		return paid;
		*/
		return true;
	}
	
	/**
	 * 	Update Order
	 *  @author Raul Muñoz 
	 */
	public void updateOrder()
	{
		if (v_POSPanel != null )
		{
			MOrder order = v_POSPanel.getM_Order();
			if (order != null)
			{			
  				f_DocumentNo.setText(order.getDocumentNo());
  				
  				// Button BPartner: enable when drafted, and order has no lines
  				setC_BPartner_ID(order.getC_BPartner_ID());
  				if(order.getDocStatus().equals(MOrder.DOCSTATUS_Drafted) && 
  						order.getLines().length == 0 )
  					f_bBPartner.setEnabled(true);
  				else
  					f_bBPartner.setEnabled(false);
  				
  			    // Button New: enabled when lines existing or order is voided
  				f_bNew.setEnabled(m_table.getRowCount() != 0 || order.getDocStatus().equals(MOrder.DOCSTATUS_Voided));
  				
  				// Button Credit Sale: enabled when drafted, with lines and not invoiced
  				if(order.getDocStatus().equals(MOrder.DOCSTATUS_Drafted) && 
  						order.getLines().length != 0 && 
  						order.getC_Invoice_ID()<=0)
  					f_bCreditSale.setEnabled(true);
  				else
  					f_bCreditSale.setEnabled(false);

  				if(!order.getDocStatus().equals(MOrder.DOCSTATUS_Voided))			
  					f_cancel.setEnabled(true);
  				else
  					f_cancel.setEnabled(false);

  			    // History Button: enabled when lines existing or order is voided
  				if(m_table.getRowCount() != 0 || order.getDocStatus().equals(MOrder.DOCSTATUS_Voided))
  	  				f_history.setEnabled(true);  	
  				else
  					f_history.setEnabled(false);
  				
  				// Button Payment: enable when (drafted, with lines) or (completed, on credit, (not invoiced or not paid) ) 
  				if((order.getDocStatus().equals(MOrder.DOCSTATUS_Drafted) && order.getLines().length != 0) ||
  	  				   (order.getDocStatus().equals(MOrder.DOCSTATUS_Completed) && 
  	  				    order.getC_DocType().getDocSubTypeSO().equalsIgnoreCase(MOrder.DocSubTypeSO_OnCredit) &&
  	  				    	(order.getC_Invoice_ID()<=0  ||
  	  				    	 !MInvoice.get(p_ctx, order.getC_Invoice_ID()).isPaid()
  	  				    	 )
  	  				   )
  	  				  )
  	  					f_cashPayment.setEnabled(true);
  	  				else 
  						f_cashPayment.setEnabled(false);	
  				
  			    // Next and Back Buttons:  enabled when lines existing or order is voided
  				if(m_table.getRowCount() != 0 || order.getDocStatus().equals(MOrder.DOCSTATUS_Voided)) {

  					if(recordposition==orderList.size()-1)
  					    f_Next.setEnabled(false); // End of order list
  					else
  	  					f_Next.setEnabled(true);

  					if(recordposition==0)
  						f_Back.setEnabled(false); // Begin of order list
  					else
  						f_Back.setEnabled(true);
  				}
  				else{
  					f_Next.setEnabled(false);
  	  				f_Back.setEnabled(false);
  				}
			}
			else
			{
				f_DocumentNo.setText("");
				setC_BPartner_ID(0);
				f_bNew.setEnabled(true);
				f_cancel.setEnabled(false);
				f_bCreditSale.setEnabled(false);
				f_history.setEnabled(true);
				f_cashPayment.setEnabled(false);
			}
			
		}
	}	

	/**
	 * 	Open Box
	 *  @author Raul Muñoz 
	 */
	public void openCashDrawer()
	{
		String port = "/dev/lp";
		
		byte data[] = new byte[] {0x1B, 0x40, 0x1C};
		try {  
            FileOutputStream m_out = null;
			if (m_out == null) {
                m_out = new FileOutputStream(port);  // No poner append = true.
            }
            m_out.write(data);
        } catch (IOException e) {
        }  
	}	

	/**
	 * 	Set Sums from Table
	 */
	void setSums(MOrder order)
	{
		int noLines = m_table.getRowCount();
		if (order == null || noLines == 0)
		{
			f_net.setText(String.valueOf(Env.ZERO.doubleValue()));
			f_total.setValue(String.valueOf(Env.ZERO.doubleValue()));
			f_tax.setValue(String.valueOf(Env.ZERO.doubleValue()));
		}
		else
		{
			// order.getMOrder().prepareIt();
			f_net.setValue(order.getTotalLines().toString());
			f_total.setValue(order.getGrandTotal().toString());
			BigDecimal total = new BigDecimal(f_total.getValue());
			BigDecimal totalNet = new BigDecimal(f_net.getValue());
			
			BigDecimal tax = total.subtract(totalNet);
			f_tax.setValue(tax.toString());

		}
	}	//	setSums

	

	@Override
	public void tableChanged(WTableModelEvent event) {
		int row = m_table.getSelectedRow();
		if (row != -1 )
		{
			Object data = m_table.getModel().getValueAt(row, 0);
			if ( data != null )
			{
				Integer id = (Integer) ((IDColumn)data).getRecord_ID();
				orderLineId = id;
				loadLine(id);
			}
		}
		if (event.getModel().equals(m_table.getModel())) //Add Minitable Source Condition
			valueChange();
	}
	
	public void valueChange() {
		
		int id = m_table.getSelectedRow();
		ListModelTable model = m_table.getModel();
		if (id != -1) {	
		IDColumn key = (IDColumn) model.getValueAt(id, 0);
		
		if ( key != null &&  key.getRecord_ID() != orderLineId )
			orderLineId = key.getRecord_ID();
			MOrderLine line = new MOrderLine(p_ctx, orderLineId, null);
			if ( line != null )
			{
				
					line.setPrice(new BigDecimal(m_table.getModel().getValueAt(id, 4).toString()));
					line.setQty(new BigDecimal(m_table.getModel().getValueAt(id, 2).toString()));
					line.saveEx();
					updateInfo();
				}
			
		}

	}
	
	private void loadLine(int lineId) {
		
		if ( lineId <= 0 )
			return;
	
		log.fine("SubCurrentLine - loading line " + lineId);
		MOrderLine ol = new MOrderLine(p_ctx, lineId, null);
		if ( ol != null )
		{
			setPrice(ol.getPriceActual());
			setQty(ol.getQtyOrdered());
		}
		
	}
	
	/**
	 * New Order
	 * @author Yamel Senih, ysenih@erpcya.com, ERPCyA http://www.erpcya.com
	 * @return void
	 */
	public void newOrder() {
		//	Do you want to use the alternate Document type?
		boolean isDocType = FDialog.ask(0, null, Msg.getMsg(p_ctx, "POS.AlternateDT"));
		setC_BPartner_ID(0);
		v_POSPanel.newOrder(isDocType);
		newLine();
		updateInfo();
		orderList.add(v_POSPanel.getM_Order().getC_Order_ID());
		recordposition = orderList.size()-1;

	}
	
	@Override
	public void onEvent(org.zkoss.zk.ui.event.Event e) throws Exception {
		String action = e.getTarget().getId();
		if (e.getTarget().equals(f_bNew)) {
				newOrder();
				e.stopPropagation();
			}
		
		else if(e.getTarget().equals(f_cashPayment)){
			payOrder();
		}
		else if (e.getTarget().equals(f_Back) ){
			previousRecord();
			updateInfo();
			return;
		}
		else if (e.getTarget().equals(f_Next) ){
			nextRecord();
			updateInfo();
			return;
		}
		else if (e.getTarget().equals(f_print))
			printOrder();
		else if(e.getTarget().equals(f_logout)){
			dispose();
			return;
		}
		else if (e.getTarget().equals(f_Up)){
			int rows = m_table.getRowCount();
			if (rows == 0)
				return;
			int row = m_table.getSelectedRow();
			row--;
			if (row < 0)
				row = 0;
			m_table.setSelectedIndex(row);
			return;
		}
		else if (e.getTarget().equals(f_Down)){
			int rows = m_table.getRowCount();
			if (rows == 0)
				return;
			int row = m_table.getSelectedRow();
			row++;
			if (row >= rows)
				row = rows - 1;
			m_table.setSelectedIndex(row);
			return;
		}
		//  Partner
		else if (e.getTarget().equals(f_bBPartner))
			{
				setParameter();
				WQueryBPartner qt = new WQueryBPartner(v_POSPanel);
				
				qt.setVisible(true);
				
				AEnv.showWindow(qt);
				findBPartner();
				if(m_table.getRowCount() > 0){
					int row = m_table.getSelectedRow();
					if (row < 0) row = 0;
					m_table.setSelectedIndex(row);
				}
		}
		else if (e.getTarget().equals(f_name1) ){
			cont++;
			if(cont<2){
				if(e.getName().equals("onFocus")) {
				WPOSKeyboard keyboard = v_POSPanel.getKeyboard(f_name1.getKeyLayoutId()); 
				keyboard.setTitle(Msg.translate(Env.getCtx(), "M_Product_ID"));
				keyboard.setPosTextField(this.f_name1);	
				if(e.getName().equals("onFocus")) {
					keyboard.setVisible(true);
					keyboard.setWidth("750px");
					keyboard.setHeight("380px");
					AEnv.showWindow(keyboard);
					findProduct();
				}
				}
			}
			else {
				cont=0;
				f_bBPartner.setFocus(true);
			}
			updateInfo();
			return;
		}
			//  Partner
		else if (e.getTarget().equals(f_name)) {
			cont++;
			if(cont<2){
				if(e.getName().equals("onFocus")) {
					setParameter();
					WQueryBPartner qt = new WQueryBPartner(v_POSPanel);
				
					qt.setVisible(true);
				
					AEnv.showWindow(qt);
					findBPartner();
					if(m_table.getRowCount() > 0){
						int row = m_table.getSelectedRow();
						if (row < 0) row = 0;
						m_table.setSelectedIndex(row);
					}
				}
			}
				else {
					cont=0;
					f_bNew.setFocus(true);
				}
				
		}
		// Cancel
		else if (e.getTarget().equals(f_cancel)){
			v_POSPanel.deleteOrder();
			updateInfo();
			v_POSPanel.setOrder(0);
			updateOrder();
			
		}
		//	Product
		else if (e.getTarget().equals(f_bSearch))
			{
				setParameter();
				WQueryProduct qt = new WQueryProduct(v_POSPanel);
				
				qt.setQueryData(m_M_PriceList_Version_ID, m_M_Warehouse_ID);
				qt.setVisible(true);
				
				AEnv.showWindow(qt);
				findProduct();
				if(m_table.getRowCount() > 0){
					int row = m_table.getSelectedRow();
					if (row < 0) row = 0;
					m_table.setSelectedIndex(row);
				}
		}
		else if (e.getTarget().equals(f_process))
			v_POSPanel.deleteOrder();		

		//	Delete
		else if (e.getTarget().equals(f_delete))
		{
			int rows = m_table.getRowCount();
			if (rows != 0)
			{
				int row = m_table.getSelectedRow();
				if (row != -1)
				{
					if ( v_POSPanel.getM_Order() != null )
						v_POSPanel.deleteLine(m_table.getSelectedRowKey());
					setQty(null);
					setPrice(null);
					orderLineId = 0;
				}
			}
			updateInfo();
			return;
		}
	
		//	Register
		if (e.getTarget().equals(f_history)) {
			
			WPosQuery qt = new WQueryTicket(v_POSPanel);
			qt.setVisible(true);
			AEnv.showWindow(qt);
			updateInfo();
			return;
		}
	
		//	Discount
		else if (e.getTarget().equals(f_discount)) {
			cont++;
			if(cont<2){
				if(e.getName().equals("onFocus")) {
				setParameter();
				WPOSKeyboard keyboard = v_POSPanel.getKeyboard(keyLayoutId); 
				keyboard.setWidth("280px");
				keyboard.setHeight("320px");
				keyboard.setPosTextField(this.f_discount);	
				AEnv.showWindow(keyboard);
				findProduct();
				if(m_table.getRowCount() > 0){
					int row = m_table.getSelectedRow();
					if (row < 0) row = 0;
					m_table.setSelectedIndex(row);
				}
				}
				MOrderLine line = new MOrderLine(p_ctx, orderLineId, null);
				if ( line != null )
				{
					line.setDiscount(new BigDecimal(f_discount.getValue().toString()));
					line.saveEx();
					updateInfo();
				}
			}
				else {
					cont=0;
					f_bBPartner.setFocus(true);
				}
			}
			
		if (action == null || action.length() == 0 || keymap == null)
			return;
		log.info( "PosSubFunctionKeys - actionPerformed: " + action);
		HashMap<Integer, MPOSKey> currentKeymap = keymap.get(currentLayout);
		
		try
		{
			int C_POSKey_ID = Integer.parseInt(action);
			MPOSKey key = currentKeymap.get(C_POSKey_ID);
			// switch layout
			if ( key.getSubKeyLayout_ID() > 0 )
			{
				currentLayout = key.getSubKeyLayout_ID();
				if(all_SubCard.getContext().equals(e.getTarget().getId())){
					all_SubCard.setVisible(true);
					popular_SubCard.setVisible(false);
				}
				else {
					all_SubCard.setVisible(false);
					popular_SubCard.setVisible(true);
				}
			}
			else
			{
				keyReturned(key);
			}
			return;
		}
		catch (Exception ex)
		{
		}
		if(m_table.equals(e.getTarget())){
			return;
		}
		updateInfo();
	}
	

	/**
	 * 	Find/Set Product & Price
	 */
	private void findProduct()
	{
		String query = f_name1.getText();
		if (query == null || query.length() == 0)
			return;
		query = query.toUpperCase();
		//	Test Number
		boolean allNumber = true;
		try
		{
			Integer.getInteger(query);
		}
		catch (Exception e)
		{
			allNumber = false;
		}
		String Value = query;
		String Name = query;
		String UPC = (allNumber ? query : null);
		String SKU = (allNumber ? query : null);
		
		MWarehousePrice[] results = null;
		setParameter();
		//
		results = MWarehousePrice.find (p_ctx,
			m_M_PriceList_Version_ID, m_M_Warehouse_ID,
			Value, Name, UPC, SKU, null);
		
		//	Set Result
		if (results.length == 0)
		{
			String message = Msg.translate(p_ctx,  "search.product.notfound");
			FDialog.warn(0, null, message + query,"");
			setM_Product_ID(0);
			setPrice(Env.ZERO);
		}
		else if (results.length == 1)
		{
			setM_Product_ID(results[0].getM_Product_ID());
			setQty(Env.ONE);
			f_name.setText(results[0].getName());
			setPrice(results[0].getPriceStd());
			saveLine();
		}
		else	//	more than one
		{
			WQueryProduct qt = new WQueryProduct(v_POSPanel);
			qt.setResults(results);
			qt.setQueryData(m_M_PriceList_Version_ID, m_M_Warehouse_ID);
			qt.setVisible(true);
		}
	}	//	findProduct
	
	/**
	 * Call back from key panel
	 */
	public void keyReturned(MPOSKey key) {
		// processed order
		if ( v_POSPanel.getM_Order() != null && v_POSPanel.getM_Order().isProcessed() )
			return;
		
		// new line
		setM_Product_ID(key.getM_Product_ID());
		setPrice();
		setQty(key.getQty());
		String saveLine = saveLine();
		if ( !saveLine.equals(null) ) {
			FDialog.error(0, this, saveLine);
		}
		updateInfo();
		return;
	}
	/**
	 * Save Line
	 * 
	 * @return true if saved
	 */
	public String saveLine() {
		MProduct product = getProduct();
		if (product == null)
			return Msg.getMsg(p_ctx, "POS.ProductNotFound");
		BigDecimal QtyOrdered  = BigDecimal.valueOf(f_quantity);
		BigDecimal PriceActual = BigDecimal.valueOf(f_price);
		if (v_POSPanel.getM_Order() == null ) {
			boolean isDocType = FDialog.ask(0, null, Msg.getMsg(p_ctx, "POS.AlternateDT"));
			v_POSPanel.newOrder(isDocType);
		}
		
		MOrderLine line = null;
		
		if ( v_POSPanel.getM_Order() != null ) {
			try {
				line = v_POSPanel.createLine(product, QtyOrdered, PriceActual);
			}
			catch (Exception e) {
				return Msg.getMsg(p_ctx, "POS.OrderLinesCannotBeCreated")+" - " + e.getMessage();
			}
			line.saveEx();
		}
		
		orderLineId = line.getC_OrderLine_ID();
		setM_Product_ID(0);
		//
		return null;
	} //	saveLine
	

	/**
	 * 	Set Query Parameter
	 */
	private void setParameter()
	{
		//	What PriceList ?
		m_M_Warehouse_ID = p_pos.getM_Warehouse_ID();
		m_M_PriceList_Version_ID = getM_PriceList_Version_ID();
	}	//	setParameter
	
	/**
	 * 	Get Product
	 *	@return product
	 */
	public MProduct getProduct()
	{
		return m_product;
	}	//	getProduct
	
	/**
	 * 	Set Price for defined product 
	 */
	public void setPrice()
	{
		if (m_product == null)
			return;
		//
		setParameter();
		MWarehousePrice result = MWarehousePrice.get (m_product,
			m_M_PriceList_Version_ID, m_M_Warehouse_ID, null);
		if (result != null)
			setPrice(result.getPriceStd());
		else
			setPrice(Env.ZERO);
	}	//	setPrice
	
	

	/**
	 * 	Update Table
	 *	@param order order
	 */
	public void updateTable (MOrder order)
	{
		int C_Order_ID = 0;
		if (order != null)
			C_Order_ID = order.getC_Order_ID();
		if (C_Order_ID == 0)
		{
			m_table.loadTable(new PO[0]);
			setSums(null);
		}
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement (m_sql, null);
			pstmt.setInt (1, C_Order_ID);
			rs = pstmt.executeQuery();
			m_table.loadTable(rs);
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, m_sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		
		for ( int i = 0; i < m_table.getRowCount(); i ++ )
		{
			IDColumn key = (IDColumn) m_table.getModel().getValueAt(i, 0);
			if ( key != null && orderLineId > 0 && key.getRecord_ID() == orderLineId )
			{
				// 31-07-2015
				m_table.setSelectedIndex(i);
				break;
			}
		}

		setSums(order);
		
	}	//	updateTable
	

	public void updateInfo()
	{
		// reload order
		if ( v_POSPanel.getM_Order() != null )
		{

			BPartnerStd = getC_BPartner_ID();
			v_POSPanel.reloadOrder();
			updateTable(v_POSPanel.getM_Order());
			updateOrder();
		}
		
	}

	/**
	 * New Line
	 */
	public void newLine() {
		setM_Product_ID(0);
		setQty(Env.ONE);
		setPrice(Env.ZERO);
		orderLineId = 0;
	} //	newLine
	
	public void setPrice(BigDecimal price) {
		if (price == null)
			price = Env.ZERO;
		f_price=price.doubleValue();
	} //	setPrice
	public void setQty(BigDecimal qty) {
		if (qty == null)
			qty = Env.ZERO;
		f_quantity=qty.doubleValue();
	} //
	
	/**
	 * 	Set Product
	 *	@param M_Product_ID id
	 */
	public void setM_Product_ID (int M_Product_ID) {
		log.fine( "PosSubProduct.setM_Product_ID=" + M_Product_ID);
		if (M_Product_ID <= 0)
			m_product = null;
		else
		{
			m_product = MProduct.get(p_ctx, M_Product_ID);
			if (m_product.get_ID() == 0)
				m_product = null;
		}
		//	Set String Info
		if (m_product != null)
		{
			f_name1.setText(m_product.getName());
		}
		else
		{
			f_name1.setText(null);
		}
		
	}	//	setM_Product_ID

	@Override
	public void tableValueChange(TableValueChangeEvent event) {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Previous Record Order
	 */
	public void previousRecord() {
		if(recordposition>0)
			v_POSPanel.setOrder(orderList.get(recordposition--));
	}

	/**
	 * Next Record Order
	 */
	public void nextRecord() {
		if(recordposition < orderList.size()-1)
			v_POSPanel.setOrder(orderList.get(recordposition++));
	}
	
	/**
	 * Get Data List Order
	 */
	public void listOrder() {
		String sql = "";
		PreparedStatement pstm;
		ResultSet rs;
		orderList = new ArrayList<Integer>();
		try 
		{
			sql=" SELECT o.C_Order_ID"
					+ " FROM C_Order o"
					+ " LEFT JOIN c_invoice i ON i.c_order_ID = o.c_order_ID"
					+ " WHERE"
					+ " (coalesce(invoiceopen(i.c_invoice_ID, 0), 0) > 0 OR o.docstatus IN ('DR', 'IP') ) AND "
					+ " o.issotrx='Y' AND "
					+ " o.ad_client_id=? "
					+ " ORDER BY o.dateordered ASC, o.datepromised ASC";
			
			pstm= DB.prepareStatement(sql, null);
			pstm.setInt (1, Env.getAD_Client_ID(Env.getCtx()));
			rs = pstm.executeQuery();
			int i = 0;
			while(rs.next()){
				orderList.add(rs.getInt(1));
				
			}
		}
		catch(Exception e)
		{
			log.severe("WSubOrder.listOrder: " + e + " -> " + sql);
		}
	}
	
}