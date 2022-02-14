/*
 *   (C) Copyright 2021 OpenText and others.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 *   Contributors:
 *     Joaquín Garzón - initial implementation
 *
 */
package com.opentext.explore.importer;

import java.util.Date;

import org.jdom2.CDATA;
import org.jdom2.Element;

import com.opentext.explore.util.DateUtil;

/**
 * 
 * @author Joaquín Garzón
 * @since 20.2
 */
public abstract class AbstractTransformer {

	protected static Element createElementField(String name, Date content) {
		return createElementField(name, DateUtil.dateToUTC(content));
	}

	protected static Element createElementField(String name, long content) {
		return createElementField(name, Long.toString(content));
	}

	protected static Element createElementField(String name, int content) {
		return createElementField(name, Integer.toString(content));
	}

	protected static Element createElementField(String name, double content) {
		return createElementField(name, Double.toString(content));
	}

	protected static Element createElementField(String name, String content) {
		Element elementField = new Element("field");
		elementField.setAttribute("name", name);

		// Exception in thread "main" org.jdom2.IllegalDataException:
		// The data "How can I intern without hurting my family?"
		// is not legal for a JDOM CDATA section: 0x0010 is not a legal
		// XML character.
		elementField.addContent(new CDATA(ignoreUnicodeGarbage(content)));

		return elementField;
	}

	protected static Element createElementField(String name, CDATA content) {
		Element elementField = new Element("field");
		elementField.setAttribute("name", name);
		elementField.addContent(content);
		return elementField;
	}

	private static String ignoreUnicodeGarbage(String str) {
		return str == null? null: str.replace("", "");
	}
}
