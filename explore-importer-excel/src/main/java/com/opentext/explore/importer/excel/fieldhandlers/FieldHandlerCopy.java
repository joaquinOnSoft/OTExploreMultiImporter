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
package com.opentext.explore.importer.excel.fieldhandlers;

import java.util.List;

import com.opentext.explore.importer.excel.pojo.TextData;

public class FieldHandlerCopy extends AbstractFieldHandler {

	@Override
	public TextData handle(TextData txtData, List<String> inputFields, List<String> outputFields) {
		String value = null;

		if (inputFields != null && inputFields.size() > 0) {
			value = getFieldValueByName(txtData, inputFields.get(0));
		}

		for (String outputField : outputFields) {
			log.debug("Copying field '" + outputField + "'  value: " + value);
			txtData = setFieldValueByName(txtData, outputField, value);
		}

		return txtData;
	}

}
