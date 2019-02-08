package com.avob.openadr.server.oadr20a.ven.controller;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.avob.openadr.model.oadr20a.Oadr20aJAXBContext;
import com.avob.openadr.model.oadr20a.exception.Oadr20aApplicationLayerException;
import com.avob.openadr.model.oadr20a.exception.Oadr20aMarshalException;
import com.avob.openadr.model.oadr20a.exception.Oadr20aUnmarshalException;
import com.avob.openadr.model.oadr20a.oadr.OadrDistributeEvent;
import com.avob.openadr.model.oadr20a.oadr.OadrResponse;
import com.avob.openadr.server.oadr20a.ven.exception.Oadr20aDistributeEventApplicationLayerException;
import com.avob.openadr.server.oadr20a.ven.service.Oadr20aVENEiEventService;

@Controller
@RequestMapping("/OpenADR2/Simple/")
public class Oadr20aVENEiEventController {

    private static final Logger LOGGER = LoggerFactory.getLogger(Oadr20aVENEiEventController.class);

    private Oadr20aJAXBContext jaxbContext;

    @Resource
    private Oadr20aVENEiEventService oadr20aVENEiEventService;

    public Oadr20aVENEiEventController() throws JAXBException {
        jaxbContext = Oadr20aJAXBContext.getInstance();
    }

    /**
     * Handle exception during request unmarshalling process
     * 
     * @param ex
     * @return
     */
    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "Can't unmarshall client request")
    @ExceptionHandler(Oadr20aUnmarshalException.class)
    public void handleOadrHttp20aException(Oadr20aUnmarshalException ex) {
        LOGGER.error(ex.getMessage());
    }

    /**
     * Handle exception during response marshalling process
     * 
     * @param ex
     * @return
     */
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Can't marshall server response")
    @ExceptionHandler(Oadr20aMarshalException.class)
    public void handleOadr20aMarshalException(Oadr20aMarshalException ex) {
        LOGGER.error(ex.getMessage(), ex);
    }

    /**
     * Handle wrong object in client request
     * 
     * @param ex
     */
    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "Unacceptable request payload for EiEventService")
    @ExceptionHandler(Oadr20aApplicationLayerException.class)
    public void handleOadr20aApplicationLayerException(Oadr20aApplicationLayerException ex) {
        LOGGER.error(ex.getMessage());
    }

    /**
     * Handle exception during oadrCreatedEvent response generation
     * 
     * @param ex
     * @return
     */
    @ExceptionHandler(Oadr20aDistributeEventApplicationLayerException.class)
    @ResponseBody
    public String handleOadr20aDistributeEventApplicationLayerException(
            Oadr20aDistributeEventApplicationLayerException ex) {
        LOGGER.warn(ex.getMessage());
        try {
            return jaxbContext.marshal(ex.getResponse());
        } catch (Oadr20aMarshalException e) {
            LOGGER.error("Can't marshall error response", e);
        }
        return null;
    }

    @RequestMapping(value = "/EiEvent", method = RequestMethod.POST)
    @ResponseBody
    public String request(@RequestBody String payload) throws Oadr20aMarshalException, Oadr20aUnmarshalException,
            Oadr20aDistributeEventApplicationLayerException, Oadr20aApplicationLayerException {

        Object unmarshal = jaxbContext.unmarshal(payload);

        String responseStr = "";

        if (unmarshal instanceof OadrDistributeEvent) {

            LOGGER.debug(payload);

            OadrDistributeEvent oadrDistributeEvent = (OadrDistributeEvent) unmarshal;

            LOGGER.info(oadrDistributeEvent.getVtnID() + " - OadrRequestEvent");

            OadrResponse response = oadr20aVENEiEventService.oadrDistributeEvent(oadrDistributeEvent);

            responseStr = jaxbContext.marshal(response);

            LOGGER.debug(responseStr);

            return responseStr;

        }

        throw new Oadr20aApplicationLayerException("Unacceptable request payload for EiEventService");
    }

}
