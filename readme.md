VeeqoImporter

Merged Order Creators

APIKEYCreator creates api.java, it takes a json file with veeqoApi and postcoderAPI

shared package contains order and customer and uploads the order to veeqo,
it also confirms that the order uploaded has a valid postcode with postcoder,
the customer email is put in the customer note attributes (this is needed for my emailer)
there is also space to put notes that customer service has requested

argos, b&q, and the range all allow to download orders as csv, rename those orders as orders.csv, then run the program

homebase orders are gathered from /xmldocs, they also update the status report and there is another program inside to generate the .hb1 file required by homebase

SeleniumEDI

this program populates the /xmldocs by logging into https://cloud.freeway.opentext.com and scrapping the orders as json files



