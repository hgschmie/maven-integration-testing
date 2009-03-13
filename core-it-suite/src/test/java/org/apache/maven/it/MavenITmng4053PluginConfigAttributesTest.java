package org.apache.maven.it;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.ResourceExtractor;

import java.io.File;
import java.util.Properties;

/**
 * This is a test set for <a href="http://jira.codehaus.org/browse/MNG-4053">MNG-4053</a>.
 * 
 * @author Benjamin Bentmann
 */
public class MavenITmng4053PluginConfigAttributesTest
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng4053PluginConfigAttributesTest()
    {
        super( "[2.0.3,)" );
    }

    /**
     * Verify that attributes in plugin configuration elements are not erroneously duplicated to other elements when
     * no plugin management is used.
     */
    public void testitWithoutPluginMngt()
        throws Exception
    {
        testit( "test-1" );
    }

    /**
     * Verify that attributes in plugin configuration elements are not erroneously duplicated to other elements when
     * plugin management is used.
     */
    public void testitWithPluginMngt()
        throws Exception
    {
        testit( "test-2" );
    }

    /**
     * Verify that attributes in plugin configuration elements are not erroneously duplicated to other elements when
     * plugin management and a profile are used.
     */
    public void testitWithPluginMngtAndProfile()
        throws Exception
    {
        testit( "test-3" );
    }

    private void testit( String test )
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-4053/" + test );

        Verifier verifier = new Verifier( testDir.getAbsolutePath() );
        verifier.setAutoclean( false );
        verifier.deleteDirectory( "target" );
        verifier.executeGoal( "validate" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        Properties props = verifier.loadProperties( "target/config.properties" );

        assertEquals( "src", props.getProperty( "domParam.children.copy.0.attributes.todir" ) );
        assertEquals( "true", props.getProperty( "domParam.children.copy.0.attributes.overwrite" ) );
        assertEquals( "2", props.getProperty( "domParam.children.copy.0.attributes" ) );

        assertEquals( "target", props.getProperty( "domParam.children.copy.0.children.fileset.0.attributes.dir" ) );
        assertEquals( null, props.getProperty( "domParam.children.copy.0.children.fileset.0.attributes.todir" ) );
        assertEquals( null, props.getProperty( "domParam.children.copy.0.children.fileset.0.attributes.overwrite" ) );
        assertEquals( "1", props.getProperty( "domParam.children.copy.0.children.fileset.0.attributes" ) );
    }

}