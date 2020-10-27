package com.azry.sps.common.utils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class XmlUtils {
	private static Map<Class, JAXBContext> contextMap = new HashMap<>();

	public static <T> String toXml(T object){
		if (object == null) {
			return null;
		}
		Class clazz = object.getClass();
		JAXBContext context = getContext(clazz);
		try {
			Marshaller marshaller = context.createMarshaller();
			StringWriter writer = new StringWriter();
			marshaller.marshal(object, writer);
			return writer.toString();

		} catch (Exception ex) {
			throw new IllegalStateException(ex);
		}
	}

	public static <T> T fromXML(String xml, Class clazz) {
		if (xml == null || clazz == null) {
			return null;
		}
		JAXBContext context = getContext(clazz);
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();
			StringReader reader = new StringReader(xml);
			//noinspection unchecked
			return (T) unmarshaller.unmarshal(reader);

		} catch (Exception ex) {
			throw new IllegalStateException(ex);
		}
	}


	public static JAXBContext getContext(Class cl){
		if (!contextMap.containsKey(cl)) {
			try {
				JAXBContext context = JAXBContext.newInstance(cl);
				contextMap.put(cl, context);
			} catch (JAXBException ex) {
				/*String message = "Unable to create JAXB context for class: " + cl.getName();
				logger.error(message);
				*/
				throw new RuntimeException(ex);
			}
		}

		return contextMap.get(cl);
	}
}
