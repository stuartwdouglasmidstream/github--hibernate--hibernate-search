/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.mapper.orm.loading.impl;

import java.util.Set;

import org.hibernate.MultiIdentifierLoadAccess;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.query.Query;

class HqlTypeQueryFactory<E, I> extends ConditionalExpressionQueryFactory<E, I> {

	private final EntityPersister entityPersister;

	HqlTypeQueryFactory(EntityPersister entityPersister, String uniquePropertyName) {
		super( uniquePropertyName );
		this.entityPersister = entityPersister;
	}

	@Override
	public Query<Long> createQueryForCount(SharedSessionContractImplementor session,
			Set<? extends Class<? extends E>> includedTypesFilter) {
		return createQueryWithTypesFilter( session,
				"select count(e) from " + entityPersister.getEntityName() + " e",
				Long.class,
				"e", includedTypesFilter );
	}

	@SuppressWarnings("unchecked")
	@Override
	public Query<I> createQueryForIdentifierListing(SharedSessionContractImplementor session,
			Set<? extends Class<? extends E>> includedTypesFilter) {
		return createQueryWithTypesFilter( session,
				"select e. " + uniquePropertyName + " from " + entityPersister.getEntityName() + " e",
				(Class<I>) entityPersister.getIdentifierType().getReturnedClass(),
				"e", includedTypesFilter );
	}

	@SuppressWarnings("unchecked")
	@Override
	public Query<E> createQueryForLoadByUniqueProperty(SessionImplementor session, String parameterName) {
		return session.createQuery(
				"select e from " + entityPersister.getEntityName()
						+ " e where " + uniquePropertyName + " in (:" + parameterName + ")",
				(Class<E>) entityPersister.getMappedClass()
		);
	}

	@Override
	@SuppressWarnings("unchecked") // Can't do better here: the underlying method has no generics
	public MultiIdentifierLoadAccess<E> createMultiIdentifierLoadAccess(SessionImplementor session) {
		return session.byMultipleIds( entityPersister.getEntityName() );
	}

	@Override
	public boolean uniquePropertyIsTheEntityId() {
		return uniquePropertyName.equals( entityPersister.getIdentifierPropertyName() );
	}

	private <T> Query<T> createQueryWithTypesFilter(SharedSessionContractImplementor session,
			String hql, Class<T> returnedType, String entityAlias,
			Set<? extends Class<? extends E>> includedTypesFilter) {
		if ( !includedTypesFilter.isEmpty() ) {
			hql += " where type(" + entityAlias + ") in (:types)";
		}
		Query<T> query = session.createQuery( hql, returnedType );
		if ( !includedTypesFilter.isEmpty() ) {
			query.setParameterList( "types", includedTypesFilter );
		}
		return query;
	}
}
