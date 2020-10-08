package org.appenders.log4j2.elasticsearch;

/*-
 * #%L
 * log4j2-elasticsearch
 * %%
 * Copyright (C) 2018 Rafal Foltynski
 * %%
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
 * #L%
 */

import io.netty.buffer.CompositeByteBuf;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.appenders.log4j2.elasticsearch.GenericItemSourcePoolTest.byteBufAllocator;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class ByteBufItemSourceTest {

    static {
        System.setProperty("io.netty.allocator.maxOrder", "7");
    }

    @Test
    public void doesNotChangeTheSource() {

        // given
        CompositeByteBuf expectedSource = createDefaultTestByteBuf();
        ItemSource itemSource =
                createTestItemSource(expectedSource, mock(ReleaseCallback.class));

        // when
        Object actualSource = itemSource.getSource();

        // then
        assertEquals(expectedSource, actualSource);
        assertTrue(expectedSource == actualSource);

    }

    @Test
    public void releaseDelegatesToGivenCallback() {

        // given
        ReleaseCallback callback = mock(ReleaseCallback.class);
        CompositeByteBuf byteBuf = createDefaultTestByteBuf();

        // then
        ItemSource source = createTestItemSource(byteBuf, callback);
        source.release();

        // then
        ArgumentCaptor<ItemSource> captor = ArgumentCaptor.forClass(ItemSource.class);
        verify(callback).completed(captor.capture());

        assertEquals(source, captor.getValue());
        assertTrue(source.getSource() == captor.getValue().getSource());

    }

    @Test
    public void releaseResetsTheSource() {

        // given
        ReleaseCallback callback = mock(ReleaseCallback.class);
        CompositeByteBuf byteBuf = spy(createDefaultTestByteBuf());

        // then
        ItemSource source = createTestItemSource(byteBuf, callback);
        source.release();

        // then
        verify(byteBuf).clear();

    }

    public static CompositeByteBuf createDefaultTestByteBuf() {
        return new CompositeByteBuf(byteBufAllocator, false, 2);
    }

    public static ByteBufItemSource createTestItemSource() {
        return createTestItemSource(createDefaultTestByteBuf(), source -> {});
    }

    public static ByteBufItemSource createTestItemSource(CompositeByteBuf byteBuf, ReleaseCallback callback) {
        return new ByteBufItemSource(byteBuf, callback);
    }

}
