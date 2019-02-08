
var JsonixModule = require('jsonix')
// Include or require PO.js so that PO variable is available
// For instance, in node.js:
var PO = require('./PO/PO').PO;

var Jsonix = JsonixModule.Jsonix;

// First we construct a Jsonix context - a factory for unmarshaller (parser)
// and marshaller (serializer)
var context = new Jsonix.Context([PO]);

// Then we create a unmarshaller
var unmarshaller = context.createUnmarshaller();

// Unmarshal an object from the XML retrieved from the URL
unmarshaller.unmarshalFile('./payload.xml',
    // This callback function will be provided
    // with the result of the unmarshalling
    function (unmarshalled) {
        // Alice Smith
        console.log(unmarshalled);
   
    });

// Create a marshaller
var marshaller = context.createMarshaller();

var doc = marshaller.marshalString({
    name: {
         localPart: 'oadrCancelPartyRegistration',
         namespaceURI: 'http:\/\/openadr.org\/oadr-2.0b\/2012\/07'
    },
    value: {
        requestID: "0",
        registrationID: "0000",
        venID: "mouaiccool"
    }
});

console.log(doc)