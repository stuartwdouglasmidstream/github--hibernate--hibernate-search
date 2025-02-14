/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.mapper.orm.coordination.outboxpolling.cluster.impl;

import java.util.List;

public interface AgentRepository {

	Agent find(Long id);

	List<Agent> findAllOrderById();

	void create(Agent agent);

	void delete(List<Agent> agents);

}
