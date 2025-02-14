/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.mapper.orm.coordination.outboxpolling.cfg.spi;

import org.hibernate.search.mapper.orm.cfg.HibernateOrmMapperSettings;
import org.hibernate.search.mapper.orm.coordination.outboxpolling.cfg.HibernateOrmMapperOutboxPollingSettings;
import org.hibernate.search.mapper.orm.coordination.outboxpolling.cluster.impl.Agent;
import org.hibernate.search.mapper.orm.coordination.outboxpolling.cluster.impl.OutboxPollingAgentAdditionalJaxbMappingProducer;
import org.hibernate.search.mapper.orm.coordination.outboxpolling.event.impl.OutboxEvent;
import org.hibernate.search.mapper.orm.coordination.outboxpolling.event.impl.OutboxPollingOutboxEventAdditionalJaxbMappingProducer;
import org.hibernate.search.util.common.annotation.Incubating;

/**
 * SPI-related settings.
 */
@Incubating
public final class HibernateOrmMapperOutboxPollingSpiSettings {

	private HibernateOrmMapperOutboxPollingSpiSettings() {
	}

	/**
	 * The prefix expected for the key of every Hibernate Search configuration property
	 * when using the Hibernate ORM mapper.
	 */
	public static final String PREFIX = HibernateOrmMapperSettings.PREFIX;

	/**
	 * Allows the user to define a specific Hibernate mapping for the {@link OutboxEvent} entity.
	 * <p>
	 * Only available when {@link HibernateOrmMapperSettings#COORDINATION_STRATEGY} is
	 * {@value HibernateOrmMapperOutboxPollingSettings#COORDINATION_STRATEGY_NAME}.
	 * <p>
	 * Expects a String value containing the xml expressing the Hibernate mapping for the entity.
	 * <p>
	 * The default for this value is {@link OutboxPollingOutboxEventAdditionalJaxbMappingProducer#ENTITY_DEFINITION}
	 */
	public static final String OUTBOXEVENT_ENTITY_MAPPING = PREFIX + Radicals.OUTBOXEVENT_ENTITY_MAPPING;

	/**
	 * Allows the user to define a specific Hibernate mapping for the {@link Agent} entity.
	 * <p>
	 * Only available when {@link HibernateOrmMapperSettings#COORDINATION_STRATEGY} is
	 * {@value HibernateOrmMapperOutboxPollingSettings#COORDINATION_STRATEGY_NAME}.
	 * <p>
	 * Expects a String value containing the xml expressing the Hibernate mapping for the entity.
	 * <p>
	 * The default for this value is {@link OutboxPollingAgentAdditionalJaxbMappingProducer#ENTITY_DEFINITION}
	 */
	public static final String AGENT_ENTITY_MAPPING = PREFIX + Radicals.AGENT_ENTITY_MAPPING;

	/**
	 * Configuration property keys without the {@link #PREFIX prefix}.
	 */
	public static final class Radicals {

		private Radicals() {
		}

		public static final String COORDINATION_PREFIX = HibernateOrmMapperSettings.Radicals.COORDINATION_PREFIX;
		public static final String OUTBOXEVENT_ENTITY_MAPPING = COORDINATION_PREFIX + CoordinationRadicals.OUTBOXEVENT_ENTITY_MAPPING;
		public static final String AGENT_ENTITY_MAPPING = COORDINATION_PREFIX + CoordinationRadicals.AGENT_ENTITY_MAPPING;
	}

	public static final class CoordinationRadicals {

		private CoordinationRadicals() {
		}

		public static final String OUTBOXEVENT_ENTITY_MAPPING = "outboxevent.entity.mapping";
		public static final String AGENT_ENTITY_MAPPING = "agent.entity.mapping";
	}

}
