/*
 * The MIT License
 *
 * Copyright 2018 jxpearce.
 *
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
 */
package io.jenkins.plugins.entry;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.model.listeners.RunListener;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * Implements {@link RunListener} extension point to
 * provide job status information to subscribers as jobs complete.
 *
 * @author Jeff Pearce (GitHub jeffpearce)
 */
@Extension
public class BuildStatusJobListener extends RunListener<Run<?, ?>>{
    private final TriggerNotifier trigger = new TriggerNotifier();

    private String trimPrefix(String s,String prefix){
        if (s != null && prefix != null && s.startsWith(prefix)) {
            return s.substring(prefix.length());
        }
        return s;
    }

    private String trimSuffix(String s,String suffix){
        if (s != null && suffix != null && s.endsWith(suffix)) {
            return s.substring(0,s.length()-suffix.length());
        }
        return s;
    }

    private void snedReq(Run<?, ?> run,boolean state){
        EntryConfiguration entry = new EntryConfiguration();
        String callback = entry.getCallback().toString();
        if (callback.isEmpty()) {
            return;
        }
        try {
            String id = trimPrefix(run.getDisplayName(),"#");
            String name = trimSuffix(run.getExternalizableId(),run.getDisplayName());
            trigger.notifyBuildStatus(entry.getCallback(),id,name,entry.getName(),state);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onStarted(Run<?, ?> run, @Nonnull TaskListener listener) {
        snedReq(run,true);
    }

    @Override
    public void onCompleted(Run<?, ?> run, @NonNull TaskListener listener) {
        snedReq(run,false);
    }
}