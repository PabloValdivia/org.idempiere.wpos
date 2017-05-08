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

package org.adempiere.pos.search;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import org.adempiere.pos.WPOS;
import org.adempiere.pos.WPOSKeyboard;
import org.adempiere.pos.WPosTextField;
import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.component.Checkbox;
import org.adempiere.webui.component.Datebox;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.component.GridFactory;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.ListboxFactory;
import org.adempiere.webui.component.Panel;
import org.adempiere.webui.component.Row;
import org.adempiere.webui.component.Rows;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.minigrid.IDColumn;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Center;
import org.zkoss.zul.North;

/**
 *	POS Query Product
 *	
 *  
 */

public class WQueryTicket extends WPosQuery
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7713957495649128816L;
	/**
	 * 	Constructor
	 */
	public WQueryTicket (WPOS posPanel)
	{
		super(posPanel);
	}	//	PosQueryProduct

	
	private WPosTextField	f_documentno;
	private Datebox			f_date;

	private int				m_c_order_id;
	private Checkbox 		f_processed;
	private Date 			date;
	/**	Internal Variables	*/
	private int				m_C_Order_ID;
	/**	Table Column Layout Info			*/
	private static ColumnInfo[] s_layout = new ColumnInfo[] 
	{
		new ColumnInfo(" ", "C_Order_ID", IDColumn.class),
		new ColumnInfo(Msg.translate(Env.getCtx(), "DocumentNo"), "DocumentNo", String.class),
		new ColumnInfo(Msg.translate(Env.getCtx(), "TotalLines"), "TotalLines", BigDecimal.class),
		new ColumnInfo(Msg.translate(Env.getCtx(), "GrandTotal"), "GrandTotal", BigDecimal.class),
		new ColumnInfo(Msg.translate(Env.getCtx(), "C_BPartner_ID"), "Name", String.class),
		new ColumnInfo(Msg.translate(Env.getCtx(), "Processed"), "Processed", Boolean.class)
	};

	/**
	 * 	Set up Panel
	 */
	protected void init()
	{
		Panel panel = new Panel();
		setVisible(true);
		Panel mainPanel = new Panel();
		Grid productLayout = GridFactory.newGridLayout();
		//	Set title window
		this.setTitle(Msg.getMsg(p_ctx, "Query"));
		this.setClosable(true);
		
		appendChild(panel);
		northPanel = new Panel();
		mainPanel.appendChild(mainLayout);
		mainPanel.setStyle("width: 100%; height: 100%; padding: 0; margin: 0");
		mainLayout.setHeight("100%");
		mainLayout.setWidth("100%");
		Center center = new Center();
		//
		North north = new North();
		north.setStyle("border: none");
		mainLayout.appendChild(north);
		north.appendChild(northPanel);
		northPanel.appendChild(productLayout);
		appendChild(mainPanel);
		productLayout.setWidth("100%");
		Rows rows = null;
		Row row = null;
		rows = productLayout.newRows();
		row = rows.newRow();
		
		Label ldoc = new Label(Msg.translate(p_ctx, "DocumentNo"));

		row.setHeight("60px");
		row.appendChild(ldoc);
		f_documentno = new WPosTextField(v_POSPanel, v_POSPanel.getOSKeyLayout_ID());
		row.appendChild(f_documentno);
		f_documentno.addEventListener("onFocus",this);
		//
		Label ldate = new Label(Msg.translate(p_ctx, "DateOrdered"));
		row.appendChild(ldate);
		f_date = new Datebox();
		f_date.setValue(Env.getContextAsDate(Env.getCtx(), "#Date"));
		f_date.addEventListener("onBlur",this);
		row.appendChild(f_date);
		
		f_processed = new Checkbox();
		f_processed.setLabel(Msg.translate(p_ctx, "Processed"));
		f_processed.setSelected(false);
		row.appendChild(f_processed);
		f_processed.addActionListener(this);
		
		//  New Line
		row = rows.newRow();
		row.setSpans("5");
		row.setHeight("65px");
		
		
		//	Center
		m_table = ListboxFactory.newDataTable();
		
		String sql = m_table.prepareTable (s_layout, "C_Order", 
				"C_POS_ID = " + p_pos.getC_POS_ID()
				, false, "C_Order")
			+ " ORDER BY Margin, QtyAvailable";

		enableButtons();
		center = new Center();
		center.setStyle("border: none");
		m_table.setWidth("100%");
		m_table.setHeight("99%");
		m_table.addActionListener(this);
		center.appendChild(m_table);
		mainLayout.appendChild(center);
		m_table.addActionListener(this);
		m_table.autoSize();
		date = new Date(Env.getContextAsDate(Env.getCtx(), "#Date").getTime());
		setResults(p_ctx, f_processed.isSelected(), f_documentno.getText(), date);
	}	//	init
	
	/**
	 * 
	 */
	@Override
	public void reset()
	{
		f_processed.setSelected(false);
		f_documentno.setText(null);
		date = new Date(Env.getContextAsDate(Env.getCtx(), "#Date").getTime());
		
		setResults(p_ctx, f_processed.isSelected(), f_documentno.getText(), date);
	}

	/**
	 * 	Set/display Results
	 *	@param results results
	 */
	public void setResults (Properties ctx, boolean processed, String doc, Date date)
	{
		String sql = "";
		try 
		{
			sql=" SELECT distinct o.C_Order_ID, o.DocumentNo, coalesce(invoiceopen(i.c_invoice_ID, 0), 0) as invoiceopen, o.GrandTotal, b.Name, o.Processed"
					+ " FROM C_Order o "
					+ " INNER JOIN C_BPartner b ON o.C_BPartner_ID=b.C_BPartner_ID"
					+ " LEFT JOIN c_invoice i on i.c_order_ID = o.c_order_ID"
					+ " WHERE o.C_POS_ID = " + p_pos.getC_POS_ID()
					+ " and coalesce(invoiceopen(i.c_invoice_ID, 0), 0)  >= 0 ";
			sql += " AND o.Processed = " + ( processed ? "'Y' " : "'N' ");
			if (doc != null && !doc.equalsIgnoreCase(""))
				sql += " AND o.DocumentNo = '" + doc + "'";
			if ( date != null)
				sql += " AND trunc(o.DateOrdered) = '"+ date +"' Order By o.DocumentNo DESC";
			PreparedStatement pstm = DB.prepareStatement(sql, null);
			ResultSet rs = pstm.executeQuery();
			m_table.loadTable(rs);
			if ( m_table.getRowCount() > 0 )
				enableButtons();
		}
		catch(Exception e)
		{
			log.severe("QueryTicket.setResults: " + e + " -> " + sql);
		}
	}	//	setResults

	/**
	 * 	Enable/Set Buttons and set ID
	 */
	protected void enableButtons()
	{
		m_c_order_id = -1;
		int row = m_table.getSelectedRow();
		boolean enabled = row != -1;
		if (enabled)
		{
			Integer ID = m_table.getSelectedRowKey();
			if (ID != null)
			{
				m_c_order_id = ID.intValue();
			}
		}
		log.info("ID=" + m_c_order_id); 
	}	//	enableButtons

	/**
	 * 	Close.
	 * 	Set Values on other panels and close
	 */
	@Override
	protected void close()
	{
		log.info("C_Order_ID=" + m_c_order_id); 
		
		if (m_c_order_id > 0)
		{
			v_POSPanel.setOrder(m_c_order_id);

		}
		dispose();
	}	//	close


	@Override
	public void onEvent(Event e) throws Exception {
		if(e.getTarget().equals(f_documentno)) {
				//	Get Keyboard Panel
				WPOSKeyboard keyboard = v_POSPanel.getKeyboard(f_documentno.getKeyLayoutId(), f_documentno);
				
				//	Set Title
				keyboard.setTitle(Msg.translate(Env.getCtx(), "M_Product_ID"));
				keyboard.setWidth("750px");
				keyboard.setHeight("380px");
				AEnv.showWindow(keyboard);
				refresh();
			
		} else if(e.getTarget().getId().equals("Refresh")) {
			refresh();
		}
		if ( e.getTarget().equals(f_processed) 
				|| e.getTarget().equals(f_date)) {
				refresh();
				return;
		}
		else if(e.getTarget().getId().equals("Ok")){
			close();
		}
		else if(e.getTarget().getId().equals("Cancel")){
			close();
		}		else if(e.getTarget().getId().equals("Reset")){
			reset();
		}
		enableButtons();
	}

	@Override
	public void refresh() {
		if(f_date.getValue()!=null)
			date = new Date(f_date.getValue().getTime());
		else
			date = null;
		setResults(p_ctx, f_processed.isSelected(), f_documentno.getText(), date);
	}

	@Override
	protected void select() {
		m_C_Order_ID = -1;
		int row = m_table.getSelectedRow();
		boolean enabled = row != -1;
		if (enabled)
		{
			Integer ID = m_table.getSelectedRowKey();
			if (ID != null)
			{
				m_C_Order_ID = ID.intValue();
			}
		}
		log.info("ID=" + m_C_Order_ID); 
	}
	@Override
	protected void cancel() {
		m_C_Order_ID = -1;
		dispose();
	}

	public int getRecord_ID() {
		return m_C_Order_ID;
	}

	public String getValue() {
		return null;
	}
	
}	//	PosQueryProduct
