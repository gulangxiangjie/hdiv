/*
 * Copyright 2004-2012 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hdiv.urlProcessor;

import javax.servlet.http.HttpServletRequest;

import org.hdiv.AbstractHDIVTestCase;
import org.hdiv.util.HDIVUtil;
import org.springframework.mock.web.MockHttpServletRequest;

public class LinkUrlProcessorTest extends AbstractHDIVTestCase {

	private LinkUrlProcessor linkUrlProcessor;

	protected void onSetUp() throws Exception {
		this.linkUrlProcessor = (LinkUrlProcessor) this.getApplicationContext().getBean("linkUrlProcessor");
	}

	public void testProcessAction() {

		HttpServletRequest request = HDIVUtil.getHttpServletRequest();
		String url = "/testAction.do";

		String result = this.linkUrlProcessor.processUrl(request, url);

		assertTrue(result.startsWith("/testAction.do?_HDIV_STATE_="));
	}

	public void testProcessActionWithContextPath() {

		MockHttpServletRequest request = (MockHttpServletRequest) HDIVUtil.getHttpServletRequest();
		request.setContextPath("/path");
		String url = "/path/testAction.do";

		String result = this.linkUrlProcessor.processUrl(request, url);

		assertTrue(result.startsWith("/path/testAction.do?_HDIV_STATE_="));
	}

	public void testProcessActionWithAnchor() {

		HttpServletRequest request = HDIVUtil.getHttpServletRequest();
		String url = "/testAction.do#anchor";

		String result = this.linkUrlProcessor.processUrl(request, url);

		assertTrue(result.startsWith("/testAction.do?_HDIV_STATE_="));
		assertTrue(result.endsWith("#anchor"));
	}

	public void testProcessActionWithParams() {

		HttpServletRequest request = HDIVUtil.getHttpServletRequest();
		String url = "/testAction.do?params=value";

		String result = this.linkUrlProcessor.processUrl(request, url);

		assertTrue(result.startsWith("/testAction.do?params=0&_HDIV_STATE_"));
	}

	public void testProcessActionParamWithoutValue() {

		HttpServletRequest request = HDIVUtil.getHttpServletRequest();
		String url = "/testAction.do?params";

		String result = this.linkUrlProcessor.processUrl(request, url);

		assertTrue(result.startsWith("/testAction.do?params=0&_HDIV_STATE_"));
	}

	public void testProcessActionRelative() {

		HttpServletRequest request = HDIVUtil.getHttpServletRequest();
		String url = "testAction.do";

		String result = this.linkUrlProcessor.processUrl(request, url);

		assertTrue(result.startsWith("/path/testAction.do?_HDIV_STATE_="));
	}

	public void testProcessActionRelative2() {

		HttpServletRequest request = HDIVUtil.getHttpServletRequest();
		String url = "../testAction.do";

		String result = this.linkUrlProcessor.processUrl(request, url);

		assertTrue(result.startsWith("/testAction.do?_HDIV_STATE_="));
	}

	public void testProcessActionRelative3() {

		MockHttpServletRequest request = (MockHttpServletRequest) HDIVUtil.getHttpServletRequest();
		request.setContextPath("/path");

		String url = "../testAction.do";

		String result = this.linkUrlProcessor.processUrl(request, url);

		assertTrue(result.equals("../testAction.do"));
	}

	public void testStripSession() {

		HttpServletRequest request = HDIVUtil.getHttpServletRequest();
		String url = "/app/list.do;jsessionid=AAAAAA?_HDIV_STATE_=14-2-8AB072360ABD8A2B2FBC484B0BC61BA4";

		String result = this.linkUrlProcessor.processUrl(request, url);

		assertTrue(result.indexOf("jsessionid") < 0);
	}

	public void testProcessAbsoluteExternalUrlWithContextPath() {

		MockHttpServletRequest request = (MockHttpServletRequest) HDIVUtil.getHttpServletRequest();
		request.setContextPath("/path");

		String url = "http://www.google.com";

		String result = this.linkUrlProcessor.processUrl(request, url);

		assertEquals("http://www.google.com", result);
	}

	public void testProcessAbsoluteExternalUrl() {

		MockHttpServletRequest request = (MockHttpServletRequest) HDIVUtil.getHttpServletRequest();

		String url = "http://www.google.com";

		String result = this.linkUrlProcessor.processUrl(request, url);

		assertEquals("http://www.google.com", result);
	}

	public void testProcessAbsoluteInternalUrlWithContextPath() {

		MockHttpServletRequest request = (MockHttpServletRequest) HDIVUtil.getHttpServletRequest();
		request.setContextPath("/path");

		String url = "http://localhost:8080/path/sample.do";

		String result = this.linkUrlProcessor.processUrl(request, url);

		assertTrue(result.startsWith("http://localhost:8080/path/sample.do?_HDIV_STATE_="));
	}

	public void testProcessAbsoluteInternalUrlWithContextPath2() {

		MockHttpServletRequest request = (MockHttpServletRequest) HDIVUtil.getHttpServletRequest();
		request.setContextPath("/diferentPath");

		String url = "http://localhost:8080/path/sample.do";

		String result = this.linkUrlProcessor.processUrl(request, url);

		assertTrue(result.startsWith("http://localhost:8080/path/sample.do"));
	}

	public void testProcessAbsoluteInternalUrl() {

		MockHttpServletRequest request = (MockHttpServletRequest) HDIVUtil.getHttpServletRequest();

		String url = "http://localhost:8080/path/sample.do";

		String result = this.linkUrlProcessor.processUrl(request, url);

		assertTrue(result.startsWith("http://localhost:8080/path/sample.do?_HDIV_STATE_="));
	}

	public void testProcessActionStartPage() {

		HttpServletRequest request = HDIVUtil.getHttpServletRequest();
		
		String url = "/testing.do"; // is a startPage
		String result = this.linkUrlProcessor.processUrl(request, url);
		assertEquals(url, result);
		
		url = "/onlyget.do"; // is a startPage only in Get requests
		result = this.linkUrlProcessor.processUrl(request, url);
		assertEquals(url, result);
		
		url = "/onlypost.do"; // is a startPage only in POST requests
		result = this.linkUrlProcessor.processUrl(request, url);
		assertTrue(result.startsWith("/onlypost.do?_HDIV_STATE_="));
	}

}
