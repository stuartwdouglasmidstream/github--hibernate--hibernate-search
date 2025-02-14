/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.mapper.javabean.mapping.impl;

import org.hibernate.search.mapper.javabean.mapping.SearchMapping;
import org.hibernate.search.mapper.javabean.reporting.impl.JavaBeanEventContextMessages;
import org.hibernate.search.engine.mapper.mapping.building.spi.MappingKey;

public final class JavaBeanMappingKey implements MappingKey<JavaBeanMappingPartialBuildState, SearchMapping> {
	private static final JavaBeanEventContextMessages MESSAGES = JavaBeanEventContextMessages.INSTANCE;

	@Override
	public String render() {
		return MESSAGES.mapping();
	}
}
