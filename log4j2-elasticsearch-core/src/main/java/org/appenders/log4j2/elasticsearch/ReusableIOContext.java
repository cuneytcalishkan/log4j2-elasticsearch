package org.appenders.log4j2.elasticsearch;

/*-
 * #%L
 * log4j2-elasticsearch
 * %%
 * Copyright (C) 2020 Rafal Foltynski
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

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.BufferRecycler;

import java.io.OutputStream;

public class ReusableIOContext extends IOContext {

    private OutputStream sourceRefReplacement;

    public ReusableIOContext(BufferRecycler br, OutputStream sourceRef, boolean managedResource) {
        super(br, sourceRef, managedResource);
        setEncoding(JsonEncoding.UTF8); // Not supported otherwise
        this.sourceRefReplacement = sourceRef;
    }

    @Override
    public Object getSourceReference() {
        return this.sourceRefReplacement;
    }

    public void setSourceReference(OutputStream replacement) {
        this.sourceRefReplacement = replacement;
    }

}
