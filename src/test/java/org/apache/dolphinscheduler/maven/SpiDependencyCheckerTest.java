/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.dolphinscheduler.maven;

import io.takari.maven.testing.TestResources;
import io.takari.maven.testing.executor.MavenExecutionResult;
import io.takari.maven.testing.executor.MavenRuntime;
import io.takari.maven.testing.executor.MavenVersions;
import io.takari.maven.testing.executor.junit.MavenJUnitTestRunner;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

@RunWith(MavenJUnitTestRunner.class)
@MavenVersions({"3.3.9", "3.5.4", "3.6.2"})
@SuppressWarnings({"JUnitTestNG", "PublicField"})
public class SpiDependencyCheckerTest {

    @Rule
    public final TestResources resources = new TestResources();

    public final MavenRuntime maven;

    public SpiDependencyCheckerTest(MavenRuntime.MavenRuntimeBuilder mavenRuntimeBuilder) throws Exception {
        this.maven = mavenRuntimeBuilder.withCliOptions("-B", "-U").build();
    }

    @Test
    public void testBasic() throws Exception
    {
        File basedir = resources.getBasedir("simplest");
        maven.forProject(basedir)
                .execute("verify")
                .assertErrorFreeLog();
    }

    @Test
    public void testErrorScopeSpi() throws Exception
    {
        File basedir = resources.getBasedir("error-scope-spi");
        MavenExecutionResult verify = maven.forProject(basedir)
                .execute("verify");
        verify.assertLogText("[ERROR] Failed to execute goal org.apache.dolphinscheduler:dolphinscheduler-maven-plugin:1.0.0-SNAPSHOT:spi-dependencies-check (default-spi-dependencies-check) on project error-scope-spi: DolphinScheduler plugin dependency org.apache.dolphinscheduler:dolphinscheduler-spi must have scope 'provided'. ");
    }

    @Test
    public void testAbstractPluginClass() throws Exception
    {
        File basedir = resources.getBasedir("abstract-plugin-class");
        maven.forProject(basedir)
                .execute("verify")
                .assertErrorFreeLog();
    }
//
    @Test
    public void testInterfacePluginClass() throws Exception
    {
        File basedir = resources.getBasedir("interface-plugin-class");
        maven.forProject(basedir)
                .execute("verify")
                .assertErrorFreeLog();
    }

    @Test
    public void testExcludedDependency() throws Exception
    {
        File basedir = resources.getBasedir("excluded-dependency");
        maven.forProject(basedir)
                .execute("verify")
                .assertErrorFreeLog();
    }

    @Test
    public void testMoreExcludedDependency() throws Exception
    {
        File basedir = resources.getBasedir("more-excluded-dependency");
        maven.forProject(basedir)
                .execute("verify")
                .assertErrorFreeLog();
    }


    @Test
    public void testErrorScopeDependency() throws Exception
    {
        File basedir = resources.getBasedir("error-scope-dependency");
        MavenExecutionResult verify = maven.forProject(basedir)
                .execute("verify");
        verify.assertLogText("[ERROR] Dolphinscheduler plugin dependency com.google.guava:guava must not have scope 'provided'. It is not part of the SPI and will not be available at runtime.");
    }


    @Test
    public void testErrorScopeButSkip() throws Exception
    {
        File basedir = resources.getBasedir("error-scope-but-skip");
        MavenExecutionResult verify = maven.forProject(basedir)
                .execute("verify");
        verify.assertErrorFreeLog();
    }
}
