/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsnyder.spring.utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import oracle.xdb.XMLType;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.util.Assert;
import org.w3c.dom.Document;
import org.springframework.data.jdbc.support.oracle.*;
/**
 *
 * @author jmittler
 */
public class StringXmlTypeHandler extends AbstractXmlTypeHandler {

    @Override
    public Object extractXmlContent(XMLType data) throws SQLException {
        return data.stringValue();
    }
    @Override
    public XMLType createXmlType(Object object, Connection conn) throws DataRetrievalFailureException {
        Assert.notNull(object, "XML input source must not be null");
        
        XMLType xml;
        try {
            if ((object instanceof String)) {
                xml = XMLType.createXML(conn, (String)object);
            }
            else if ((object instanceof InputStream)) {
                xml = XMLType.createXML(conn, (InputStream)object);
            }
            else if ((object instanceof Document)) {
                xml = XMLType.createXML(conn, (Document)object);
            }
            else {
                throw new IllegalArgumentException("The provided value is not a supported type: " + object.getClass().getName());
            }
        } catch (SQLException sqle) {
            throw new DataRetrievalFailureException("Error while creating XMLType", sqle);
        }

        return xml;
    }
    
}
