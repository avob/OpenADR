package com.avob.openadr.server.oadr20b.vtn.service.ei;

import com.avob.openadr.server.common.vtn.models.ven.Ven;

public interface Oadr20bVTNEiService {

	public Object request(Ven ven, Object payload);

	public String getServiceName();

}
