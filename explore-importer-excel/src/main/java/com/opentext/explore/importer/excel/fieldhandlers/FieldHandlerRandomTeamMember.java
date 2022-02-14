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

public class FieldHandlerRandomTeamMember extends AbstractFieldHandler {
	private static final String[] names = { "Chang, Monica", "Romanoff, Natasha", "Richards, Reed", "Wagner, Kurt",
			"Parker, Peter", "Howlett, James", "Natchios, Elektra", "LeBeau, Remy" };
	
	protected int getRandom(int min, int max) {
		return (int) (Math.random() * ((max - min) + 1)) + min;
	}	
	
	/**
	 * Choose a team member name, randomly, from a predefined list of 8 users (team members) 
	 * 
	 * @param txtData      - Text data container (represents a row in an Excel or
	 *                     CSV file)
	 * @param inputFields  - ignored, could be an empty list
	 * @param outputFields - Output field where the team member value will be assigned
	 */
	@Override
	public TextData handle(TextData txtData, List<String> inputFields, List<String> outputFields) {

		for (String outputField : outputFields) {
			int index = getRandom(0, names.length - 1);
			txtData = setFieldValueByName(txtData, outputField, names[index]);
		}

		return txtData;
	}
}
