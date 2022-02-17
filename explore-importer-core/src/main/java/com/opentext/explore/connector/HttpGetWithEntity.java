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
package com.opentext.explore.connector;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

/**
 * Apache HttpClient GET with body
 * SEE: https://stackoverflow.com/questions/12535016/apache-httpclient-get-with-body
 * @author Joaquín Garzón
 * @since 20.2
 */
public class HttpGetWithEntity extends HttpEntityEnclosingRequestBase {
    public final static String METHOD_NAME = "GET";

	@Override
	public String getMethod() {
		return METHOD_NAME;
	}

}
