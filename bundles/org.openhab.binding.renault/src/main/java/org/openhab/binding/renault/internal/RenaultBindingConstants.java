/**
 * Copyright (c) 2010-2021 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.renault.internal;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.core.thing.ThingTypeUID;

/**
 * The {@link RenaultBindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Doug Culnane - Initial contribution
 */
@NonNullByDefault
public class RenaultBindingConstants {

    private static final String BINDING_ID = "renault";

    // List of all Thing Type UIDs
    public static final ThingTypeUID THING_TYPE_CAR = new ThingTypeUID(BINDING_ID, "car");

    // List of all Channel ids
    public static final String CHANNEL_BATTERY_LEVEL = "batterylevel";
    public static final String CHANNEL_HVAC_STATUS = "hvacstatus";
    public static final String CHANNEL_IMAGE = "image";
    public static final String CHANNEL_LOCATION = "location";
    public static final String CHANNEL_ODOMETER = "odometer";
}
