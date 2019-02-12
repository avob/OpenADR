package com.avob.openadr.server.oadr20b.ven.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;

import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedPartyRegistrationType;
import com.avob.openadr.server.oadr20b.ven.VtnSessionConfiguration;

@Service
public class StatePersistenceService {

	private static final String CURRENT_REGISTRATION_PATH = "oadrCreatePartyRegistration.xml";

	private Oadr20bJAXBContext jaxbContext;

	public StatePersistenceService() throws JAXBException {
		jaxbContext = Oadr20bJAXBContext.getInstance();
	}

	private static Path getPath(String venId, VtnSessionConfiguration vtnConfiguration) {
		return Paths.get(venId + "." + vtnConfiguration.getVtnId() + "." + CURRENT_REGISTRATION_PATH);
	}

	public synchronized void persistRegistration(String venId, VtnSessionConfiguration vtnConfiguration,
			OadrCreatedPartyRegistrationType registration) throws Oadr20bMarshalException, IOException {
		String marshalRoot = jaxbContext.marshalRoot(registration);
		Path path = getPath(venId, vtnConfiguration);
		try (BufferedWriter writer = Files.newBufferedWriter(path)) {
			writer.write(marshalRoot);
		}
	}

	public synchronized void deleteRegistration(String venId, VtnSessionConfiguration vtnConfiguration)
			throws IOException {
		Path path = getPath(venId, vtnConfiguration);
		if (Files.exists(path)) {
			Files.delete(path);
		}
	}

	public synchronized OadrCreatedPartyRegistrationType loadRegistration(String venId,
			VtnSessionConfiguration vtnConfiguration) throws IOException, Oadr20bUnmarshalException {
		Path path = getPath(venId, vtnConfiguration);
		if (Files.exists(path)) {
			try (BufferedReader reader = Files.newBufferedReader(path)) {
				String collect = reader.lines().collect(Collectors.joining("\n"));
				reader.close();
				return jaxbContext.unmarshal(collect, OadrCreatedPartyRegistrationType.class);
			}
		}
		return null;
	}

}
