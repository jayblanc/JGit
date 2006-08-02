/*
 *  Copyright (C) 2006  Shawn Pearce <spearce@spearce.org>
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License, version 2.1, as published by the Free Software Foundation.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 */
package org.spearce.egit.ui;

import org.eclipse.osgi.util.NLS;

public class UIText extends NLS
{
    public static String SharingWizard_windowTitle;

    public static String SharingWizard_failed;

    public static String GenericOperationFailed;

    public static String ExistingOrNewPage_title;

    public static String ExistingOrNewPage_description;

    public static String ExistingOrNewPage_groupHeader;

    public static String ExistingOrNewPage_useExisting;

    public static String ExistingOrNewPage_createNew;

    public static String Decorator_failedLazyLoading;
    
    static
    {
        initializeMessages(
            UIText.class.getPackage().getName() + ".uitext",
            UIText.class);
    }
}
