/******************************************************************************
 * Product: iDempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2012 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.idempiere.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.model.*;
import org.compiere.util.KeyNamePair;

/** Generated Interface for C_POS
 *  @author iDempiere (generated) 
 *  @version Release 6.2
 */
@SuppressWarnings("all")
public interface I_C_POS 
{

    /** TableName=C_POS */
    public static final String Table_Name = "C_POS";

    /** AD_Table_ID=748 */
    public static final int Table_ID = 748;

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 2 - Client 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(2);

    /** Load Meta Data */

    /** Column name AD_Client_ID */
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";

	/** Get Client.
	  * Client/Tenant for this installation.
	  */
	public int getAD_Client_ID();

    /** Column name AD_Org_ID */
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

	/** Set Organization.
	  * Organizational entity within client
	  */
	public void setAD_Org_ID (int AD_Org_ID);

	/** Get Organization.
	  * Organizational entity within client
	  */
	public int getAD_Org_ID();

    /** Column name AD_Sequence_ID */
    public static final String COLUMNNAME_AD_Sequence_ID = "AD_Sequence_ID";

	/** Set Sequence.
	  * Document Sequence
	  */
	public void setAD_Sequence_ID (int AD_Sequence_ID);

	/** Get Sequence.
	  * Document Sequence
	  */
	public int getAD_Sequence_ID();

	public org.compiere.model.I_AD_Sequence getAD_Sequence() throws RuntimeException;

    /** Column name AD_Tab_ID */
    public static final String COLUMNNAME_AD_Tab_ID = "AD_Tab_ID";

	/** Set Tab.
	  * Tab within a Window
	  */
	public void setAD_Tab_ID (int AD_Tab_ID);

	/** Get Tab.
	  * Tab within a Window
	  */
	public int getAD_Tab_ID();

	public org.compiere.model.I_AD_Tab getAD_Tab() throws RuntimeException;

    /** Column name AD_User_ID */
    public static final String COLUMNNAME_AD_User_ID = "AD_User_ID";

	/** Set User/Contact.
	  * User within the system - Internal or Business Partner Contact
	  */
	public void setAD_User_ID (int AD_User_ID);

	/** Get User/Contact.
	  * User within the system - Internal or Business Partner Contact
	  */
	public int getAD_User_ID();

	public org.compiere.model.I_AD_User getAD_User() throws RuntimeException;

    /** Column name AD_Val_Rule_ID */
    public static final String COLUMNNAME_AD_Val_Rule_ID = "AD_Val_Rule_ID";

	/** Set Dynamic Validation.
	  * Dynamic Validation Rule
	  */
	public void setAD_Val_Rule_ID (int AD_Val_Rule_ID);

	/** Get Dynamic Validation.
	  * Dynamic Validation Rule
	  */
	public int getAD_Val_Rule_ID();

	public org.compiere.model.I_AD_Val_Rule getAD_Val_Rule() throws RuntimeException;

    /** Column name AD_Window_ID */
    public static final String COLUMNNAME_AD_Window_ID = "AD_Window_ID";

	/** Set Window.
	  * Data entry or display window
	  */
	public void setAD_Window_ID (int AD_Window_ID);

	/** Get Window.
	  * Data entry or display window
	  */
	public int getAD_Window_ID();

	public org.compiere.model.I_AD_Window getAD_Window() throws RuntimeException;

    /** Column name AutoLogoutDelay */
    public static final String COLUMNNAME_AutoLogoutDelay = "AutoLogoutDelay";

	/** Set Auto Logout Delay.
	  * Automatically logout if terminal inactive for this period
	  */
	public void setAutoLogoutDelay (int AutoLogoutDelay);

	/** Get Auto Logout Delay.
	  * Automatically logout if terminal inactive for this period
	  */
	public int getAutoLogoutDelay();

    /** Column name CashDrawer */
    public static final String COLUMNNAME_CashDrawer = "CashDrawer";

	/** Set CashDrawer	  */
	public void setCashDrawer (String CashDrawer);

	/** Get CashDrawer	  */
	public String getCashDrawer();

    /** Column name CashTransferBankAccount_ID */
    public static final String COLUMNNAME_CashTransferBankAccount_ID = "CashTransferBankAccount_ID";

	/** Set Transfer Cash trx to.
	  * Bank Account on which to transfer all Cash transactions
	  */
	public void setCashTransferBankAccount_ID (int CashTransferBankAccount_ID);

	/** Get Transfer Cash trx to.
	  * Bank Account on which to transfer all Cash transactions
	  */
	public int getCashTransferBankAccount_ID();

	public org.compiere.model.I_C_BankAccount getCashTransferBankAccount() throws RuntimeException;

    /** Column name C_BankAccount_ID */
    public static final String COLUMNNAME_C_BankAccount_ID = "C_BankAccount_ID";

	/** Set Bank Account.
	  * Account at the Bank
	  */
	public void setC_BankAccount_ID (int C_BankAccount_ID);

	/** Get Bank Account.
	  * Account at the Bank
	  */
	public int getC_BankAccount_ID();

	public org.compiere.model.I_C_BankAccount getC_BankAccount() throws RuntimeException;

    /** Column name C_BPartnerCashTrx_ID */
    public static final String COLUMNNAME_C_BPartnerCashTrx_ID = "C_BPartnerCashTrx_ID";

	/** Set Template B.Partner.
	  * Business Partner used for creating new Business Partners on the fly
	  */
	public void setC_BPartnerCashTrx_ID (int C_BPartnerCashTrx_ID);

	/** Get Template B.Partner.
	  * Business Partner used for creating new Business Partners on the fly
	  */
	public int getC_BPartnerCashTrx_ID();

	public org.compiere.model.I_C_BPartner getC_BPartnerCashTrx() throws RuntimeException;

    /** Column name C_CashBook_ID */
    public static final String COLUMNNAME_C_CashBook_ID = "C_CashBook_ID";

	/** Set Cash Book.
	  * Cash Book for recording petty cash transactions
	  */
	public void setC_CashBook_ID (int C_CashBook_ID);

	/** Get Cash Book.
	  * Cash Book for recording petty cash transactions
	  */
	public int getC_CashBook_ID();

	public org.compiere.model.I_C_CashBook getC_CashBook() throws RuntimeException;

    /** Column name C_Charge_ID */
    public static final String COLUMNNAME_C_Charge_ID = "C_Charge_ID";

	/** Set Charge.
	  * Additional document charges
	  */
	public void setC_Charge_ID (int C_Charge_ID);

	/** Get Charge.
	  * Additional document charges
	  */
	public int getC_Charge_ID();

	public org.compiere.model.I_C_Charge getC_Charge() throws RuntimeException;

    /** Column name C_DocType_ID */
    public static final String COLUMNNAME_C_DocType_ID = "C_DocType_ID";

	/** Set Document Type.
	  * Document type or rules
	  */
	public void setC_DocType_ID (int C_DocType_ID);

	/** Get Document Type.
	  * Document type or rules
	  */
	public int getC_DocType_ID();

	public org.compiere.model.I_C_DocType getC_DocType() throws RuntimeException;

    /** Column name C_POS_ID */
    public static final String COLUMNNAME_C_POS_ID = "C_POS_ID";

	/** Set POS Terminal.
	  * Point of Sales Terminal
	  */
	public void setC_POS_ID (int C_POS_ID);

	/** Get POS Terminal.
	  * Point of Sales Terminal
	  */
	public int getC_POS_ID();

    /** Column name C_POSKeyLayout_ID */
    public static final String COLUMNNAME_C_POSKeyLayout_ID = "C_POSKeyLayout_ID";

	/** Set POS Key Layout.
	  * POS Function Key Layout
	  */
	public void setC_POSKeyLayout_ID (int C_POSKeyLayout_ID);

	/** Get POS Key Layout.
	  * POS Function Key Layout
	  */
	public int getC_POSKeyLayout_ID();

	public org.compiere.model.I_C_POSKeyLayout getC_POSKeyLayout() throws RuntimeException;

    /** Column name C_POS_UU */
    public static final String COLUMNNAME_C_POS_UU = "C_POS_UU";

	/** Set C_POS_UU	  */
	public void setC_POS_UU (String C_POS_UU);

	/** Get C_POS_UU	  */
	public String getC_POS_UU();

    /** Column name Created */
    public static final String COLUMNNAME_Created = "Created";

	/** Get Created.
	  * Date this record was created
	  */
	public Timestamp getCreated();

    /** Column name CreatedBy */
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";

	/** Get Created By.
	  * User who created this records
	  */
	public int getCreatedBy();

    /** Column name DeliveryRule */
    public static final String COLUMNNAME_DeliveryRule = "DeliveryRule";

	/** Set Delivery Rule.
	  * Defines the timing of Delivery
	  */
	public void setDeliveryRule (String DeliveryRule);

	/** Get Delivery Rule.
	  * Defines the timing of Delivery
	  */
	public String getDeliveryRule();

    /** Column name Description */
    public static final String COLUMNNAME_Description = "Description";

	/** Set Description.
	  * Optional short description of the record
	  */
	public void setDescription (String Description);

	/** Get Description.
	  * Optional short description of the record
	  */
	public String getDescription();

    /** Column name ElectronicScales */
    public static final String COLUMNNAME_ElectronicScales = "ElectronicScales";

	/** Set ElectronicScales.
	  * Allows to define path for Device Electronic Scales e.g. /dev/ttyS0/
	  */
	public void setElectronicScales (String ElectronicScales);

	/** Get ElectronicScales.
	  * Allows to define path for Device Electronic Scales e.g. /dev/ttyS0/
	  */
	public String getElectronicScales();

    /** Column name Help */
    public static final String COLUMNNAME_Help = "Help";

	/** Set Comment/Help.
	  * Comment or Hint
	  */
	public void setHelp (String Help);

	/** Get Comment/Help.
	  * Comment or Hint
	  */
	public String getHelp();

    /** Column name InvoiceRule */
    public static final String COLUMNNAME_InvoiceRule = "InvoiceRule";

	/** Set Invoice Rule.
	  * Frequency and method of invoicing 
	  */
	public void setInvoiceRule (String InvoiceRule);

	/** Get Invoice Rule.
	  * Frequency and method of invoicing 
	  */
	public String getInvoiceRule();

    /** Column name IsActive */
    public static final String COLUMNNAME_IsActive = "IsActive";

	/** Set Active.
	  * The record is active in the system
	  */
	public void setIsActive (boolean IsActive);

	/** Get Active.
	  * The record is active in the system
	  */
	public boolean isActive();

    /** Column name IsEnableProductLookup */
    public static final String COLUMNNAME_IsEnableProductLookup = "IsEnableProductLookup";

	/** Set Enable POS Product Lookup	  */
	public void setIsEnableProductLookup (boolean IsEnableProductLookup);

	/** Get Enable POS Product Lookup	  */
	public boolean isEnableProductLookup();

    /** Column name IsModifyPrice */
    public static final String COLUMNNAME_IsModifyPrice = "IsModifyPrice";

	/** Set Modify Price.
	  * Allow modifying the price
	  */
	public void setIsModifyPrice (boolean IsModifyPrice);

	/** Get Modify Price.
	  * Allow modifying the price
	  */
	public boolean isModifyPrice();

    /** Column name IsPOSRequiredPIN */
    public static final String COLUMNNAME_IsPOSRequiredPIN = "IsPOSRequiredPIN";

	/** Set Is POS Required PIN.
	  * Indicates that a Supervisor Pin is mandatory to execute some tasks e.g. (Change Price , Offer Discount , Delete POS Line)
	  */
	public void setIsPOSRequiredPIN (boolean IsPOSRequiredPIN);

	/** Get Is POS Required PIN.
	  * Indicates that a Supervisor Pin is mandatory to execute some tasks e.g. (Change Price , Offer Discount , Delete POS Line)
	  */
	public boolean isPOSRequiredPIN();

    /** Column name LIT_isShowWindowProduct */
    public static final String COLUMNNAME_LIT_isShowWindowProduct = "LIT_isShowWindowProduct";

	/** Set Show Window Product	  */
	public void setLIT_isShowWindowProduct (boolean LIT_isShowWindowProduct);

	/** Get Show Window Product	  */
	public boolean isLIT_isShowWindowProduct();

    /** Column name LIT_POSType */
    public static final String COLUMNNAME_LIT_POSType = "LIT_POSType";

	/** Set POS Type	  */
	public void setLIT_POSType (String LIT_POSType);

	/** Get POS Type	  */
	public String getLIT_POSType();

    /** Column name MeasureRequestCode */
    public static final String COLUMNNAME_MeasureRequestCode = "MeasureRequestCode";

	/** Set Measure Request Code.
	  * String for  taking measurement from Device Electronic Scales
	  */
	public void setMeasureRequestCode (String MeasureRequestCode);

	/** Get Measure Request Code.
	  * String for  taking measurement from Device Electronic Scales
	  */
	public String getMeasureRequestCode();

    /** Column name M_PriceList_ID */
    public static final String COLUMNNAME_M_PriceList_ID = "M_PriceList_ID";

	/** Set Price List.
	  * Unique identifier of a Price List
	  */
	public void setM_PriceList_ID (int M_PriceList_ID);

	/** Get Price List.
	  * Unique identifier of a Price List
	  */
	public int getM_PriceList_ID();

	public org.compiere.model.I_M_PriceList getM_PriceList() throws RuntimeException;

    /** Column name M_Warehouse_ID */
    public static final String COLUMNNAME_M_Warehouse_ID = "M_Warehouse_ID";

	/** Set Warehouse.
	  * Storage Warehouse and Service Point
	  */
	public void setM_Warehouse_ID (int M_Warehouse_ID);

	/** Get Warehouse.
	  * Storage Warehouse and Service Point
	  */
	public int getM_Warehouse_ID();

	public org.compiere.model.I_M_Warehouse getM_Warehouse() throws RuntimeException;

    /** Column name Name */
    public static final String COLUMNNAME_Name = "Name";

	/** Set Name.
	  * Alphanumeric identifier of the entity
	  */
	public void setName (String Name);

	/** Get Name.
	  * Alphanumeric identifier of the entity
	  */
	public String getName();

    /** Column name OSK_KeyLayout_ID */
    public static final String COLUMNNAME_OSK_KeyLayout_ID = "OSK_KeyLayout_ID";

	/** Set On Screen Keyboard layout.
	  * The key layout to use for on screen keyboard for text fields.
	  */
	public void setOSK_KeyLayout_ID (int OSK_KeyLayout_ID);

	/** Get On Screen Keyboard layout.
	  * The key layout to use for on screen keyboard for text fields.
	  */
	public int getOSK_KeyLayout_ID();

	public org.compiere.model.I_C_POSKeyLayout getOSK_KeyLayout() throws RuntimeException;

    /** Column name OSNP_KeyLayout_ID */
    public static final String COLUMNNAME_OSNP_KeyLayout_ID = "OSNP_KeyLayout_ID";

	/** Set On Screen Number Pad layout.
	  * The key layout to use for on screen number pad for numeric fields.
	  */
	public void setOSNP_KeyLayout_ID (int OSNP_KeyLayout_ID);

	/** Get On Screen Number Pad layout.
	  * The key layout to use for on screen number pad for numeric fields.
	  */
	public int getOSNP_KeyLayout_ID();

	public org.compiere.model.I_C_POSKeyLayout getOSNP_KeyLayout() throws RuntimeException;

    /** Column name PINEntryTimeout */
    public static final String COLUMNNAME_PINEntryTimeout = "PINEntryTimeout";

	/** Set PIN Entry Timeout.
	  * PIN Entry Timeout - the amount of time from initial display until the PIN entry dialog times out, in milliseconds.
	  */
	public void setPINEntryTimeout (int PINEntryTimeout);

	/** Get PIN Entry Timeout.
	  * PIN Entry Timeout - the amount of time from initial display until the PIN entry dialog times out, in milliseconds.
	  */
	public int getPINEntryTimeout();

    /** Column name PrinterName */
    public static final String COLUMNNAME_PrinterName = "PrinterName";

	/** Set Printer Name.
	  * Name of the Printer
	  */
	public void setPrinterName (String PrinterName);

	/** Get Printer Name.
	  * Name of the Printer
	  */
	public String getPrinterName();

    /** Column name SalesRep_ID */
    public static final String COLUMNNAME_SalesRep_ID = "SalesRep_ID";

	/** Set Sales Representative.
	  * Sales Representative or Company Agent
	  */
	public void setSalesRep_ID (int SalesRep_ID);

	/** Get Sales Representative.
	  * Sales Representative or Company Agent
	  */
	public int getSalesRep_ID();

    /** Column name TicketClassName */
    public static final String COLUMNNAME_TicketClassName = "TicketClassName";

	/** Set Ticket Handler Class Name.
	  * Java Classname for Ticket Handler
	  */
	public void setTicketClassName (String TicketClassName);

	/** Get Ticket Handler Class Name.
	  * Java Classname for Ticket Handler
	  */
	public String getTicketClassName();

    /** Column name Updated */
    public static final String COLUMNNAME_Updated = "Updated";

	/** Get Updated.
	  * Date this record was updated
	  */
	public Timestamp getUpdated();

    /** Column name UpdatedBy */
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

	/** Get Updated By.
	  * User who updated this records
	  */
	public int getUpdatedBy();
}
