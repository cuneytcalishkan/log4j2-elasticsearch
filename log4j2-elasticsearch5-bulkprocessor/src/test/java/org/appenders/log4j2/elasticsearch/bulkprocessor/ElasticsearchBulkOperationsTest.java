package org.appenders.log4j2.elasticsearch.bulkprocessor;

/*-
 * #%L
 * log4j2-elasticsearch
 * %%
 * Copyright (C) 2017 Rafal Foltynski
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */


import org.appenders.log4j2.elasticsearch.BatchOperations;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.BulkActionIntrospector;
import org.elasticsearch.action.index.IndexRequest;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.mockito.Mockito.spy;

public class ElasticsearchBulkOperationsTest {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void throwsOnBatchBuilderCreate() {

        // given
        BatchOperations<BulkRequest> batchOperations = createDefaultTestBulkRequestBatchOperations();

        expectedException.expect(UnsupportedOperationException.class);

        // when
        batchOperations.createBatchBuilder();

    }

    @Test
    public void createsBatchItemWithSource() {

        // given
        BatchOperations<BulkRequest> batchOperations = createDefaultTestBulkRequestBatchOperations();


        String expectedPayload = "expectedPayload";

        // when
        IndexRequest batchItem = (IndexRequest) batchOperations.createBatchItem("testIndex", expectedPayload);

        // then
        Assert.assertEquals(expectedPayload, new BulkActionIntrospector().getPayload(batchItem));
        Assert.assertEquals("index", batchItem.opType().getLowercase());

    }

    private BatchOperations<BulkRequest> createDefaultTestBulkRequestBatchOperations() {
        BulkProcessorObjectFactory factory = BulkProcessorObjectFactoryTest
                .createTestObjectFactoryBuilder()
                .build();

        return spy(factory.createBatchOperations());
    }

}