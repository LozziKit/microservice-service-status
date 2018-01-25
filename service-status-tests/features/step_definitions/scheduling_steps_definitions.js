var {defineSupportCode} = require('cucumber');
let request = require("superagent");
let chai = require("chai");
let {SERVER_URL, SERVICES_SERVER_URL} = require("./config");

let assert = chai.assert;

let services = [];

defineSupportCode(function ({And, But, Given, Then, When}) {
    Given(/^I have a service Server running$/, () => request.get(SERVER_URL).then((err,res)=> err));

    Given(/^I have the following services:$/, (table,callback) => {

        for(let i=1;i<table.rawTable.length;i++){
            let service = table.rawTable[i];
            services.push({
                "name":service[0],
                "url" : service[1],
                "description" : service[2],
                "interval": parseInt(service[3]),
                "port":parseInt(service[1].split(":")[2].split("/")[0])
            })
        }

        let admin_endpoint = SERVICES_SERVER_URL+"/__admin/mappings";

        services.forEach(s => {
            request
                .post(admin_endpoint)
                .send(
                    {
                        "request": {
                            "method": "GET",
                            "url": ("/"+s.url.split(":")[2].split("/")[1])
                        },
                        "response": {
                            "status": 200,
                            "body": "Hello world!",
                            "headers": {
                                "Content-Type": "text/plain"
                            }
                        }
                    }
                ) // sends a JSON post body
                .set('accept', 'json')
                .then(callback(null))
                .catch((err,res)=>callback(err));
        })
    });

    Given(/^they're added to the server for tracking$/, function (callback) {
        services.forEach(s => {
            request.post(SERVER_URL+"/services")
                .send(s).set('accept','json')
                .then(callback(null))
                .catch((err,res)=>callback(err));
        })
    });
    Then(/^they're supposed to have been contacted after their delay$/,{timeout:100000}, function (callback) {
        services.forEach(s => {
            setTimeout(() => {
                request.post(SERVICES_SERVER_URL+"/__admin/requests/count")
                    .send({
                        "method": "GET",
                        "url": "/"+s.url.split("/")[3]
                    }).set('accept','json')
                    .then((response)=>callback(assert.equal(response.body.count,1)))
                    .catch((err,res)=>callback(err));
            },s.interval*1010)

        })
    });

    When(/^a service's delay is changed$/, {timeout:1000000},function (callback) {

        request
            .get(SERVER_URL+"/services")
            .accept('json')
            .then((res)=> {
                let TheOneService = res.body[0];
                request
                    .put(SERVER_URL+TheOneService.location)
                    .set('Content-Type','application/json')
                    .send({
                        "description": TheOneService.description,
                        "interval": TheOneService.interval+1,
                        "name": TheOneService.name,
                        "port": TheOneService.port,
                        "url": TheOneService.url
                    }).then(callback(null))
                    .catch(callback)
            })
            .catch(callback);

    });

    Then(/^its next trigger's fire time shall be updated$/, function (callback) {
        request
            .delete(SERVICES_SERVER_URL+"/__admin/requests")
            .then(res =>{
                setTimeout(() => {
                    request.post(SERVICES_SERVER_URL+"/__admin/requests/count")
                        .send({
                            "method": "GET",
                            "url": "/"+services[0].url.split("/")[3]
                        }).set('accept','json')
                        .then((response)=>callback(assert.equal(response.body.count,0)))
                        .catch((err,res)=>callback(err));
                }, 2*services[0].interval);
            }).catch(callback)
    });

    When(/^a service is deleted$/, function (callback) {
        request
            .get(SERVER_URL+"/services")
            .accept('json')
            .then((res)=> {
                let TheOneService = res.body[0];
                request
                    .delete(SERVER_URL+TheOneService.location)
                    .then(callback(null))
                    .catch(callback)
            })
            .catch(callback);
    });
    Then(/^the scheduler shouldn't have any trace of future check$/, function (callback) {

        request
            .get(SERVER_URL+"/services")
            .accept('json')
            .then((res)=> {
                let timeout = services[0].interval;
                setTimeout(()=>{
                    request
                        .post(SERVICES_SERVER_URL+"/__admin/requests/count")
                        .send({
                            "method": "GET",
                            "url": "/"+services[0].url.split("/")[3]
                        })
                        .set('accept','json')
                        .then((response)=>callback(assert.equal(0,0)))
                        .catch((err,res)=>callback(err))},timeout)

            })
            .catch(callback);

    });
});
