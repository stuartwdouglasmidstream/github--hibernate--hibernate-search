/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.documentation.mapper.orm.bridge.propertybridge.bridgedelement;


import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManagerFactory;

import org.hibernate.search.documentation.mapper.orm.bridge.propertybridge.param.InvoiceLineItem;
import org.hibernate.search.documentation.mapper.orm.bridge.propertybridge.param.InvoiceLineItemCategory;
import org.hibernate.search.documentation.testsupport.BackendConfigurations;
import org.hibernate.search.documentation.testsupport.DocumentationSetupHelper;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.hibernate.search.mapper.pojo.mapping.definition.programmatic.TypeMappingStep;
import org.hibernate.search.util.impl.integrationtest.mapper.orm.OrmUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class PropertyBridgeBridgedElementIT {
	@Parameterized.Parameters(name = "{0}")
	public static List<?> params() {
		return DocumentationSetupHelper.testParamsForBothAnnotationsAndProgrammatic(
				BackendConfigurations.simple(),
				mapping -> {
					//tag::programmatic[]
					TypeMappingStep invoiceMapping = mapping.type( Invoice.class );
					invoiceMapping.indexed();
					invoiceMapping.property( "lineItems" ).binder( new InvoiceLineItemsSummaryBinder() );
					//end::programmatic[]
				} );
	}

	@Parameterized.Parameter
	@Rule
	public DocumentationSetupHelper setupHelper;

	private EntityManagerFactory entityManagerFactory;

	@Before
	public void setup() {
		entityManagerFactory = setupHelper.start().setup( Invoice.class );
	}

	@Test
	public void smoke() {
		OrmUtils.withinJPATransaction( entityManagerFactory, entityManager -> {
			Invoice invoice = new Invoice();
			invoice.getLineItems()
					.add( new InvoiceLineItem( InvoiceLineItemCategory.BOOK, new BigDecimal( "5.99" ) ) );
			invoice.getLineItems()
					.add( new InvoiceLineItem( InvoiceLineItemCategory.BOOK, new BigDecimal( "8.99" ) ) );
			invoice.getLineItems()
					.add( new InvoiceLineItem( InvoiceLineItemCategory.BOOK, new BigDecimal( "15.99" ) ) );
			invoice.getLineItems()
					.add( new InvoiceLineItem( InvoiceLineItemCategory.SHIPPING, new BigDecimal( "7.99" ) ) );
			entityManager.persist( invoice );
		} );

		OrmUtils.withinJPATransaction( entityManagerFactory, entityManager -> {
			SearchSession searchSession = Search.session( entityManager );

			List<Invoice> result = searchSession.search( Invoice.class )
					.where( f -> f.bool()
							.must( f.range().field( "lineItems.total" )
									.atLeast( new BigDecimal( "20.0" ) ) )
							.must( f.range().field( "lineItems.shipping" )
									.atMost( new BigDecimal( "10.0" ) ) )
					)
					.fetchHits( 20 );

			assertThat( result ).hasSize( 1 );
		} );
	}

}
