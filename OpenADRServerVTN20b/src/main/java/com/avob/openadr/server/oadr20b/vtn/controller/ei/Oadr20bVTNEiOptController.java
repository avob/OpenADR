package com.avob.openadr.server.oadr20b.vtn.controller.ei;

import java.security.Principal;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.Oadr20bUrlPath;
import com.avob.openadr.model.oadr20b.exception.Oadr20bApplicationLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelOptType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateOptType;
import com.avob.openadr.model.oadr20b.oadr.OadrPayload;
import com.avob.openadr.server.oadr20b.vtn.VtnConfig;
import com.avob.openadr.server.oadr20b.vtn.exception.eiopt.Oadr20bCancelOptApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.exception.eiopt.Oadr20bCreateOptApplicationLayerException;
import com.avob.openadr.server.oadr20b.vtn.service.Oadr20bVTNEiOptService;
import com.avob.openadr.server.oadr20b.vtn.service.XmlSignatureService;

@Controller
@RequestMapping(Oadr20bUrlPath.OADR_BASE_PATH)
public class Oadr20bVTNEiOptController {

    private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bVTNEiOptController.class);

    private Oadr20bJAXBContext jaxbContext;

	@Resource
	private VtnConfig vtnConfig;
    
    @Resource
    private Oadr20bVTNEiOptService oadr20bVTNEiOptService;

    @Resource
    private XmlSignatureService xmlSignatureService;

    public Oadr20bVTNEiOptController() throws JAXBException {
        jaxbContext = Oadr20bJAXBContext.getInstance();
    }

    @PreAuthorize("hasRole('ROLE_VEN') AND hasRole('ROLE_REGISTERED')")
    @RequestMapping(value = Oadr20bUrlPath.EI_OPT_SERVICE, method = RequestMethod.POST)
    @ResponseBody
    public String request(@RequestBody String payload, Principal principal)
            throws Oadr20bUnmarshalException, Oadr20bMarshalException, Oadr20bApplicationLayerException,
            Oadr20bCreateOptApplicationLayerException, Oadr20bCancelOptApplicationLayerException,
            Oadr20bXMLSignatureValidationException, Oadr20bXMLSignatureException {

        Object unmarshal = jaxbContext.unmarshal(payload, vtnConfig.getValidateOadrPayloadAgainstXsd());

        String username = principal.getName();

        if (unmarshal instanceof OadrPayload) {

            OadrPayload oadrPayload = (OadrPayload) unmarshal;

            return handle(username, payload, oadrPayload);

        } else if (unmarshal instanceof OadrCreateOptType) {

            LOGGER.info(username + " - OadrCreateOptType");

            OadrCreateOptType oadrCreateOptType = (OadrCreateOptType) unmarshal;

            return handle(username, oadrCreateOptType, false);

        } else if (unmarshal instanceof OadrCancelOptType) {

            LOGGER.info(username + " - OadrCancelOptType");

            OadrCancelOptType oadrCancelOptType = (OadrCancelOptType) unmarshal;

            return handle(username, oadrCancelOptType, false);

        }

        throw new Oadr20bApplicationLayerException("Unacceptable request payload for OadrPoll");
    }

    private String handle(String username, String raw, OadrPayload oadrPayload)
            throws Oadr20bMarshalException, Oadr20bApplicationLayerException, Oadr20bXMLSignatureValidationException,
            Oadr20bCancelOptApplicationLayerException, Oadr20bCreateOptApplicationLayerException,
            Oadr20bXMLSignatureException {

        xmlSignatureService.validate(raw, oadrPayload);
        if (oadrPayload.getOadrSignedObject().getOadrCreateOpt() != null) {

            LOGGER.info(username + " - OadrCreateOptType signed");

            return handle(username, oadrPayload.getOadrSignedObject().getOadrCreateOpt(), true);

        } else if (oadrPayload.getOadrSignedObject().getOadrCancelOpt() != null) {

            LOGGER.info(username + " - OadrCancelOptType signed");

            return handle(username, oadrPayload.getOadrSignedObject().getOadrCancelOpt(), true);

        }

        throw new Oadr20bApplicationLayerException("Unacceptable request payload for OadrPoll");
    }

    private String handle(String username, OadrCreateOptType oadrCreateOpt, boolean signed)
            throws Oadr20bCreateOptApplicationLayerException, Oadr20bMarshalException, Oadr20bXMLSignatureException {

        oadr20bVTNEiOptService.checkMatchUsernameWithRequestVenId(username, oadrCreateOpt);

        return oadr20bVTNEiOptService.oadrCreateOpt(oadrCreateOpt, signed);

    }

    private String handle(String username, OadrCancelOptType oadrCancelOpt, boolean signed)
            throws Oadr20bCancelOptApplicationLayerException, Oadr20bMarshalException, Oadr20bXMLSignatureException {

        oadr20bVTNEiOptService.checkMatchUsernameWithRequestVenId(username, oadrCancelOpt);

        return oadr20bVTNEiOptService.oadrCancelOptType(oadrCancelOpt, signed);

    }

}
