package com.jmc.jisuucc.map;

import java.io.File;

import org.apache.felix.dm.annotation.api.Start;
import org.mapeditor.core.Map;
import org.mapeditor.io.TMXMapReader;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Component;

@Component
public class Example {
	
	@Start
	public void start(BundleContext bc) throws Exception {
		Map map = new TMXMapReader().readMap("assets" + File.separatorChar + "map1.tmx");
		System.out.println("Started no problemo! " + map);
	}
}
