package org.adempiere.pos;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.math.BigDecimal;

import org.adempiere.pos.service.I_POSPanel;
import org.adempiere.webui.component.ListModelTable;
import org.adempiere.webui.component.ListboxFactory;
import org.adempiere.webui.component.WListbox;
import org.adempiere.webui.event.WTableModelEvent;
import org.adempiere.webui.event.WTableModelListener;
import org.compiere.minigrid.IDColumn;
import org.compiere.model.MOrderLine;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Center;

public class WPOSOrderLinePanel extends WPosSubPanel implements WTableModelListener, I_POSPanel,FocusListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4023538043556457231L;

	/** The Table					*/
	private WListbox		m_table;
	
	/** The Table					*/
	public POSOrderLineTableHandle m_TableHandle;
	
	/**
	 * Constructor
	 * 
	 * @param posPanel POS Panel
	 */
	public WPOSOrderLinePanel(WPOS posPanel) {
		super(posPanel);
	}
	/**	Table Name			*/
	private static final String	TABLE_NAME	= "POS_OrderLine_v";

	/**	Current Order Line	*/
	private int 			m_C_OrderLine_ID = 0;
	
	/**	Column Names		*/
	public static final String 	PRODUCTNAME	= "ProductName";
	public static final String 	QTYORDERED  = "QtyOrdered";
	public static final String 	C_UOM_ID    = "C_UOM_ID";
	public static final String 	PRICEACTUAL = "PriceActual";
	public static final String 	LINENETAMT  = "LineNetAmt";
	public static final String 	GRANDTOTAL  = "GrandTotal";
	/**	Column Position		*/
	public static final int	POSITION_C_ORDER_ID 	= 0;
	public static final int	POSITION_QTYORDERED 	= 2;
	public static final int	POSITION_PRICE 			= 4;
	public static final int	POSITION_LINENETAMT 	= 5;
	public static final int	POSITION_GRANDTOTAL 	= 7;
	

	/**	Logger				*/
	private static CLogger log = CLogger.getCLogger(WPOSOrderLinePanel.class);
	@Override
	protected void init() {
		m_table = ListboxFactory.newDataTable();

		
		
		m_TableHandle = new POSOrderLineTableHandle(m_table);
		m_TableHandle.prepareTable();
		m_table.getModel().addTableModelListener(this);

		m_table.setColumnClass(4, BigDecimal.class, true);
		Center center = new Center();
		center.appendChild(m_table);
		m_table.setWidth("100%");
		m_table.setHeight("95%");
		m_table.addActionListener(this);
		center.setStyle("border: none; height:95%;");
		m_table.loadTable(new PO[0]);
		m_table.setClass("Table-OrderLine");
		m_table.setColumnReadOnly(POSITION_QTYORDERED, true);
		appendChild(center);
	}

	@Override
	public void refreshPanel() {
		if (!v_POSPanel.hasOrder()) {
			m_table.loadTable(new PO[0]);
		}
		//	Load Data
		m_TableHandle.loadTable(v_POSPanel.getC_Order_ID());
		//	
		for ( int i = 0; i < m_table.getRowCount(); i ++ ) {
			IDColumn key = (IDColumn) m_table.getModel().getValueAt(i, 0);
			if ( key != null && m_C_OrderLine_ID > 0 && key.getRecord_ID() == m_C_OrderLine_ID )
			{
				m_table.setSelectedIndex(i);
				break;
			}
		}
	}
	
	@Override
	public void onEvent(Event arg0) throws Exception {
		String action = arg0.getTarget().getId();
		if (action == null || action.length() == 0)
			return;
		log.info( "POSOrderLinePanel - actionPerformed: " + action);
		if(arg0.getTarget().equals(m_table)){
			m_table.setColumnReadOnly(POSITION_QTYORDERED, false);
			return;
		}
		//	Product
		//	Refresh All
		v_POSPanel.refreshPanel();
	}

	@Override
	public void tableChanged(WTableModelEvent event) {
		int row = m_table.getSelectedRow();
		if (event.getModel().equals(m_table.getModel())){ //Add Minitable Source Condition
		if (row != -1 )
		{
			Object data = m_table.getModel().getValueAt(row, 0);
			if ( data != null )
			{
				Integer id = (Integer) ((IDColumn)data).getRecord_ID();
				m_C_OrderLine_ID = id;
			}
		}else {
			return;
		}
		int id = m_table.getSelectedRow();
		
			if (id != -1) {	
				ListModelTable model = m_table.getModel();
				IDColumn key = (IDColumn) model.getValueAt(id, 0);
				m_table.getModel().removeTableModelListener(this);			
			if ( key != null &&  key.getRecord_ID() != m_C_OrderLine_ID )
				m_C_OrderLine_ID = key.getRecord_ID();
			BigDecimal qty = new BigDecimal(m_table.getModel().getValueAt(id, POSOrderLineTableHandle.POSITION_QTYORDERED).toString());
			MOrderLine line = new MOrderLine(p_ctx, m_C_OrderLine_ID, null);
			if ( line != null )
			{
					line.setPrice(new BigDecimal(m_table.getModel().getValueAt(id, 4).toString()));
					line.setQty(new BigDecimal(m_table.getModel().getValueAt(id, 2).toString()));
					line.saveEx();
					refreshPanel();
					BigDecimal grandTotal = line.getLineNetAmt();
					m_table.getModel().setValueAt(line.getLineNetAmt(), id, POSOrderLineTableHandle.POSITION_LINENETAMT);
					if(!line.getC_Tax().getRate().equals(null)){
						grandTotal.multiply(line.getC_Tax().getRate());
					} 
					m_table.getModel().setValueAt(grandTotal, id, POSOrderLineTableHandle.POSITION_GRANDTOTAL);
					if(qty.compareTo(Env.ZERO) <= 0){
						line.delete(true);
						m_table.setColumnReadOnly(POSITION_QTYORDERED, true);
					}
					v_POSPanel.reloadOrder();
					v_POSPanel.refreshPanel();
					m_table.getModel().addTableModelListener(this);
					
					return;
				}
			}
			
		}
	}
	public void valueChange() {
		
		int id = m_table.getSelectedRow();
		ListModelTable model = m_table.getModel();
		if (id != -1) {	
			IDColumn key = (IDColumn) model.getValueAt(id, 0);
			
			if ( key != null &&  key.getRecord_ID() != m_C_OrderLine_ID ){
				m_C_OrderLine_ID = key.getRecord_ID();
				MOrderLine line = new MOrderLine(p_ctx, m_C_OrderLine_ID, null);
				if ( line != null )
				{
					
						line.setPrice(new BigDecimal(m_table.getModel().getValueAt(id, 4).toString()));
						line.setQty(new BigDecimal(m_table.getModel().getValueAt(id, 2).toString()));
						line.saveEx();
						v_POSPanel.reloadOrder();
						v_POSPanel.refreshPanel();
					}
				
			}
		}

	}
	/**
	 * 	Focus Gained
	 *	@param e
	 */
	public void focusGained (FocusEvent e) {
		log.info("POSOrderLinePanel - focusGained: " + e);		
	}	//	focusGained
		

	/**
	 * 	Focus Lost
	 *	@param e
	 */
	public void focusLost (FocusEvent e) {
		if (e.isTemporary())
			return;
		log.info( "POSProductPanel - focusLost");
		v_POSPanel.refreshPanel();
	}	//	focusLost

	@Override
	public String validatePanel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void changeViewPanel() {
		// TODO Auto-generated method stub
		
	}

}
