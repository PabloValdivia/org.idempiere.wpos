CREATE OR REPLACE VIEW adempiere.pos_orderline_v
AS SELECT c_orderline.c_orderline_id,
    c_orderline.ad_client_id,
    c_orderline.ad_org_id,
    c_orderline.isactive,
    c_orderline.created,
    c_orderline.createdby,
    c_orderline.updated,
    c_orderline.updatedby,
    c_orderline.c_order_id,
    c_orderline.line,
    c_orderline.c_bpartner_id,
    c_orderline.c_bpartner_location_id,
    c_orderline.dateordered,
    c_orderline.datepromised,
    c_orderline.datedelivered,
    c_orderline.dateinvoiced,
    c_orderline.description,
    c_orderline.m_product_id,
    c_orderline.m_warehouse_id,
    c_orderline.c_uom_id,
    c_orderline.qtyordered,
    c_orderline.qtyreserved,
    c_orderline.qtydelivered,
    c_orderline.qtyinvoiced,
    c_orderline.m_shipper_id,
    c_orderline.c_currency_id,
    c_orderline.pricelist,
    c_orderline.priceactual,
    c_orderline.pricelimit,
    c_orderline.linenetamt,
    c_orderline.discount,
    c_orderline.freightamt,
    c_orderline.c_charge_id,
    c_orderline.c_tax_id,
    c_orderline.s_resourceassignment_id,
    c_orderline.ref_orderline_id,
    c_orderline.m_attributesetinstance_id,
    c_orderline.isdescription,
    c_orderline.processed,
    c_orderline.qtyentered,
    c_orderline.priceentered,
    c_orderline.c_project_id,
    c_orderline.pricecost,
    c_orderline.qtylostsales,
    c_orderline.c_projectphase_id,
    c_orderline.c_projecttask_id,
    c_orderline.rrstartdate,
    c_orderline.rramt,
    c_orderline.c_campaign_id,
    c_orderline.c_activity_id,
    c_orderline.user1_id,
    c_orderline.user2_id,
    c_orderline.ad_orgtrx_id,
    c_orderline.link_orderline_id,
    c_orderline.pp_cost_collector_id,
    c_orderline.m_promotion_id,
    c_orderline.c_orderline_uu,
    c_orderline.createshipment,
    c_orderline.createproduction,
    p.name AS productname,
    um.uomsymbol,
    ct.taxindicator,
    o.grandtotal
   FROM c_orderline c_orderline
     LEFT JOIN m_product p ON p.m_product_id = c_orderline.m_product_id
     LEFT JOIN c_uom um ON um.c_uom_id = p.c_uom_id
     LEFT JOIN c_tax ct ON ct.c_tax_id = c_orderline.c_tax_id
     LEFT JOIN c_order o ON o.c_order_id = c_orderline.c_order_id;