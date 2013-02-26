/* Copyright (c) 2011 Danish Maritime Authority
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package dk.dma.ais.bus;

import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.Assert;
import org.junit.Test;

import dk.dma.ais.bus.configuration.AisBusConfiguration;
import dk.dma.ais.bus.configuration.consumer.StdoutConsumerConfiguration;
import dk.dma.ais.bus.configuration.filter.DownSampleFilterConfiguration;
import dk.dma.ais.bus.configuration.filter.DuplicateFilterConfiguration;
import dk.dma.ais.bus.configuration.filter.FilterConfiguration;
import dk.dma.ais.bus.configuration.provider.RoundRobinTcpClientConfiguration;
import dk.dma.ais.bus.consumer.StdoutConsumer;
import dk.dma.ais.bus.provider.RoundRobinTcpClient;

public class AisBusTest {

    //@Test
    public void tcpConsumer() {
        // Make ais bus configuration
        // TODO

        // Make ais bus
        AisBus aisBus = new AisBus();
        
        // Start AisBus
        aisBus.start();       
               
        // Make consumer
        AisBusConsumer consumer = new StdoutConsumer();
//        SourceFilter filter = new SourceFilter();
//        filter.addFilterValue("region", "9");
//        consumer.getFilters().addFilter(filter);
        // Register consumer
        aisBus.registerConsumer(consumer);

        // Make providers
        AisBusProvider provider1 = new RoundRobinTcpClient("ais163.sealan.dk:65262", 10, 10);
        AisBusProvider provider2 = new RoundRobinTcpClient("iala63.sealan.dk:4712,iala68.sealan.dk:4712", 10, 10);
        AisBusProvider provider3 = new RoundRobinTcpClient("10.10.5.124:25251", 10, 10);
        AisBusProvider provider4 = new RoundRobinTcpClient("10.10.5.144:65061", 10, 10);
        
        aisBus.registerProvider(provider1);
        aisBus.registerProvider(provider2);
        aisBus.registerProvider(provider3);
        aisBus.registerProvider(provider4);


        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    
    @Test
    public void confTest() throws JAXBException {
        AisBusConfiguration conf = new AisBusConfiguration();
        // Bus Filters
        conf.getFilters().add(new DownSampleFilterConfiguration());
        conf.getFilters().add(new DuplicateFilterConfiguration());
        // Provider
        RoundRobinTcpClientConfiguration rrReader = new RoundRobinTcpClientConfiguration();
        rrReader.setHostsPorts("ais163.sealan.dk:65262");
        rrReader.getFilters().add(new DownSampleFilterConfiguration(300));
        conf.getProviders().add(rrReader);        
        // Consumer
        StdoutConsumerConfiguration stdoutConsumer = new StdoutConsumerConfiguration();
        stdoutConsumer.getFilters().add(new DownSampleFilterConfiguration(600));
        conf.getConsumers().add(stdoutConsumer);
        
        // Save
        AisBusConfiguration.save("aisbus.xml", conf);
        
        // Load
        conf = AisBusConfiguration.load("aisbus.xml");      
        Assert.assertEquals(conf.getBusQueueSize(), 10000);
    }
    
    @Test
    public void confLoadTest() throws JAXBException {
        AisBusConfiguration conf = AisBusConfiguration.load("src/main/resources/aisbus-example.xml");
        Assert.assertEquals(conf.getBusQueueSize(), 10000);
        List<FilterConfiguration> filters = conf.getFilters();
        for (FilterConfiguration filter : filters) {
            if (filter instanceof DownSampleFilterConfiguration) {
                Assert.assertEquals(((DownSampleFilterConfiguration)filter).getSamplingRate(), 60);
            }
            else if (filter instanceof DuplicateFilterConfiguration) {
                Assert.assertEquals(((DuplicateFilterConfiguration)filter).getWindowSize(), 10000);
            }
            else {
                Assert.fail();
            }
        }
    }
    
    //@Test
    public void factoryTest() throws JAXBException, InterruptedException {
        AisBus aisBus = AisBusFactory.get("src/main/resources/aisbus-example.xml");
        
        aisBus.getThread().join();
        
    }


}
