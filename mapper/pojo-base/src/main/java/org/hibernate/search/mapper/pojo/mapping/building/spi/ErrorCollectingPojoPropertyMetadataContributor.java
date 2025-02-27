/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.mapper.pojo.mapping.building.spi;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.search.mapper.pojo.model.additionalmetadata.building.spi.PojoAdditionalMetadataCollectorPropertyNode;

public final class ErrorCollectingPojoPropertyMetadataContributor implements PojoPropertyMetadataContributor {

	private List<PojoPropertyMetadataContributor> children;

	@Override
	public void contributeAdditionalMetadata(PojoAdditionalMetadataCollectorPropertyNode collector) {
		if ( hasContent() ) {
			for ( PojoPropertyMetadataContributor child : children ) {
				child.contributeAdditionalMetadata( collector );
			}
		}
	}

	@Override
	public void contributeMapping(PojoMappingCollectorPropertyNode collector) {
		if ( hasContent() ) {
			for ( PojoPropertyMetadataContributor child : children ) {
				try {
					child.contributeMapping( collector );
				}
				catch (RuntimeException e) {
					collector.failureCollector().add( e );
				}
			}
		}
	}

	public ErrorCollectingPojoPropertyMetadataContributor add(PojoPropertyMetadataContributor child) {
		initChildren();
		this.children.add( child );
		return this;
	}

	public boolean hasContent() {
		return children != null && !children.isEmpty();
	}

	private void initChildren() {
		if ( this.children == null ) {
			this.children = new ArrayList<>();
		}
	}
}
