/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.smartdata.server.engine.cmdlet.agent;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValueFactory;
import org.junit.Test;
import org.smartdata.server.engine.cmdlet.agent.messages.AgentToMaster;
import org.smartdata.server.engine.cmdlet.agent.messages.MasterToAgent;
import org.smartdata.server.engine.CmdletManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class TestAgentMaster {

  @Test
  public void testAgentMaster() throws Exception {
    CmdletManager statusUpdater = mock(CmdletManager.class);
    String host = "127.0.0.1";
    String port = "30000";
    Config config = ConfigFactory.empty().withValue(AgentConstants.AKKA_REMOTE_HOST_KEY,
        ConfigValueFactory.fromAnyRef(host))
        .withValue(AgentConstants.AKKA_REMOTE_PORT_KEY,
            ConfigValueFactory.fromAnyRef(port));
    AgentMaster master = new AgentMaster(config, statusUpdater);

    // Wait for master to start
    Thread.sleep(1000);

    Object answer = master.askMaster(AgentToMaster.RegisterNewAgent.getInstance());
    assertTrue(answer instanceof MasterToAgent.AgentRegistered);
    MasterToAgent.AgentRegistered registered = (MasterToAgent.AgentRegistered) answer;
    assertEquals(0, registered.getAgentId().getId());
    assertTrue(master.canAcceptMore());
  }
}