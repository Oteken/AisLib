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
package dk.dma.ais.bus.configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import dk.dma.ais.bus.configuration.consumer.AisBusConsumerConfiguration;
import dk.dma.ais.bus.configuration.provider.AisBusProviderConfiguration;

@XmlRootElement
public class AisBusConfiguration extends AisBusComponentConfiguration {

    private int busPullMaxElements = 1000;
    private int consumerPullMaxElements = 1000;
    private int busQueueSize = 10000;
    private int consumerQueueSize = 10000;
    
    private List<AisBusProviderConfiguration> providers = new ArrayList<>();
    private List<AisBusConsumerConfiguration> consumers = new ArrayList<>();

    public AisBusConfiguration() {

    }

    public int getBusPullMaxElements() {
        return busPullMaxElements;
    }

    public void setBusPullMaxElements(int busPullMaxElements) {
        this.busPullMaxElements = busPullMaxElements;
    }

    public int getConsumerPullMaxElements() {
        return consumerPullMaxElements;
    }

    public void setConsumerPullMaxElements(int consumerPullMaxElements) {
        this.consumerPullMaxElements = consumerPullMaxElements;
    }

    public int getBusQueueSize() {
        return busQueueSize;
    }

    public void setBusQueueSize(int busQueueSize) {
        this.busQueueSize = busQueueSize;
    }

    public int getConsumerQueueSize() {
        return consumerQueueSize;
    }

    public void setConsumerQueueSize(int consumerQueueSize) {
        this.consumerQueueSize = consumerQueueSize;
    }
    
    @XmlElement(name = "provider")
    public List<AisBusProviderConfiguration> getProviders() {
        return providers;
    }
    
    public void setProviders(List<AisBusProviderConfiguration> providers) {
        this.providers = providers;
    }
    
    @XmlElement(name = "consumer")
    public List<AisBusConsumerConfiguration> getConsumers() {
        return consumers;
    }
    
    public void setConsumers(List<AisBusConsumerConfiguration> consumers) {
        this.consumers = consumers;
    }
    
    public static void save(String filename, final AisBusConfiguration conf) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(AisBusConfiguration.class);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        m.marshal(conf, new File(filename));
    }
    
    public static AisBusConfiguration load(String filename) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(AisBusConfiguration.class);
        Unmarshaller um = context.createUnmarshaller();
        return (AisBusConfiguration)um.unmarshal(new File(filename));        
    }

}
