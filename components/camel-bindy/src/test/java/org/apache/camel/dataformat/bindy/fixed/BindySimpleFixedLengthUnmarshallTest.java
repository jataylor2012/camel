/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.dataformat.bindy.fixed;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;
import org.apache.camel.dataformat.bindy.annotation.FixedLengthRecord;
import org.apache.camel.test.junit4.TestSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import static org.junit.Assert.assertEquals;

@ContextConfiguration
public class BindySimpleFixedLengthUnmarshallTest extends AbstractJUnit4SpringContextTests {

    private static final transient Log LOG = LogFactory.getLog(BindySimpleFixedLengthUnmarshallTest.class);

    private static final String URI_MOCK_RESULT = "mock:result";
    private static final String URI_MOCK_ERROR = "mock:error";
    private static final String URI_DIRECT_START = "direct:start";

    @Produce(uri = URI_DIRECT_START)
    private ProducerTemplate template;

    @EndpointInject(uri = URI_MOCK_RESULT)
    private MockEndpoint result;

    @EndpointInject(uri = URI_MOCK_ERROR)
    private MockEndpoint error;

    private String expected;

    @Test
    @DirtiesContext
    public void testUnMarshallMessage() throws Exception {

    	expected = "10A9PaulineMISINXD12345678BUYShare2500.45USD08-01-2009";

        template.sendBody(expected);

        result.expectedMessageCount(1);
        result.assertIsSatisfied();
    }

    public static class ContextConfig extends RouteBuilder {
        BindyFixedLengthDataFormat camelDataFormat = new BindyFixedLengthDataFormat("org.apache.camel.dataformat.bindy.fixed");

        public void configure() {
            from(URI_DIRECT_START)
            .unmarshal(camelDataFormat)
            .to(URI_MOCK_RESULT);
        }

    }
    
    @FixedLengthRecord(length=55, paddingChar=' ')
    public static class Order {

        @DataField(pos = 1, length=2)
        private int orderNr;

        @DataField(pos = 3, length=2)
        private String clientNr;

        @DataField(pos = 5, length=7)
        private String firstName;

        @DataField(pos = 12, length=1)
        private String lastName;

        @DataField(pos = 13, length=4)
        private String instrumentCode;

        @DataField(pos = 17, length=10)
        private String instrumentNumber;

        @DataField(pos = 27, length=3)
        private String orderType;

        @DataField(pos = 30, length=5)
        private String instrumentType;

        @DataField(pos = 35, precision = 2, length=7)
        private BigDecimal amount;

        @DataField(pos = 42, length=3)
        private String currency;

        @DataField(pos = 45, length=10, pattern = "dd-MM-yyyy")
        private Date orderDate;

        public int getOrderNr() {
            return orderNr;
        }

        public void setOrderNr(int orderNr) {
            this.orderNr = orderNr;
        }

        public String getClientNr() {
            return clientNr;
        }

        public void setClientNr(String clientNr) {
            this.clientNr = clientNr;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getInstrumentCode() {
            return instrumentCode;
        }

        public void setInstrumentCode(String instrumentCode) {
            this.instrumentCode = instrumentCode;
        }

        public String getInstrumentNumber() {
            return instrumentNumber;
        }

        public void setInstrumentNumber(String instrumentNumber) {
            this.instrumentNumber = instrumentNumber;
        }

        public String getOrderType() {
            return orderType;
        }

        public void setOrderType(String orderType) {
            this.orderType = orderType;
        }

        public String getInstrumentType() {
            return instrumentType;
        }

        public void setInstrumentType(String instrumentType) {
            this.instrumentType = instrumentType;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public Date getOrderDate() {
            return orderDate;
        }

        public void setOrderDate(Date orderDate) {
            this.orderDate = orderDate;
        }

        @Override
        public String toString() {
            return "Model : " + Order.class.getName() + " : " + this.orderNr + ", " + this.orderType + ", " + String.valueOf(this.amount) + ", " + this.instrumentCode + ", "
                   + this.instrumentNumber + ", " + this.instrumentType + ", " + this.currency + ", " + this.clientNr + ", " + this.firstName + ", " + this.lastName + ", "
                   + String.valueOf(this.orderDate);
        }
    }

}
