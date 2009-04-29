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

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.maven.it.util.ResourceExtractor;

/**
 * MNG-4091 - Bad plugin descriptor error handling
 */
public class MavenITmng4091BadPluginDescriptorTest
    extends AbstractMavenIntegrationTestCase
{

    public MavenITmng4091BadPluginDescriptorTest()
    {
        super( "[2.1.0,)" ); // only test in 2.1.0+
    }

    public void testitMNG4091_InvalidDescriptor()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-4091/invalid" );

        Verifier verifier = new Verifier( testDir.getAbsolutePath() );
        verifier.setAutoclean( false );

        try
        {
            verifier.executeGoal( "validate" );

            fail( "should throw an error during execution." );
        }
        catch ( VerificationException e )
        {
            // expected...it'd be nice if we could get the specifics of the exception right here...
        }
        finally
        {
            verifier.resetStreams();
        }


        List logFile = verifier.loadFile( verifier.getBasedir(), verifier.getLogFileName(), false );

        String msg = "Plugin's descriptor contains the wrong version: 2.0-SNAPSHOT";

        boolean foundMessage = false;
        for ( Iterator it = logFile.iterator(); it.hasNext(); )
        {
            String line = (String) it.next();
            if ( line.indexOf( msg ) > -1 )
            {
                foundMessage = true;
                break;
            }
        }

        assertTrue( "User-friendly message was not found in output.", foundMessage );
    }

    public void testitMNG4091_PluginDependency()
        throws Exception
    {
        File testDir = ResourceExtractor.simpleExtractResources( getClass(), "/mng-4091/plugin-dependency" );

        Verifier verifier = new Verifier( testDir.getAbsolutePath() );
        verifier.setAutoclean( false );

        verifier.executeGoal( "validate" );
        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        Properties props = verifier.loadProperties( "target/plugin-dependency.properties" );
        assertTrue( props.isEmpty() );
    }
}
