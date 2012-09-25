/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var 
    pgmValue = '', 
    lvlValue = '', 
    hnValue = '',
    txtValue = '', 
    txtValue_raw = '', 
    dateValue = '',
    solrUrl = '', 
    sortValue = 'generated+desc',
    startRow = 0,
    maxRows = 0;

function generate_solr_url() {

    $.ajax({
        url: 'request.jsp',
        data: {
            programTxt: pgmValue,
            severityTxt: lvlValue,
            hostnameTxt: hnValue,
            msgTxt: txtValue,
            generatedTxt: dateValue,
            sortbyTxt: sortValue,
            startRow: startRow,
            solrUrl: solrUrl
        },
        dataType: 'xml',
        beforeSend: function() {  list_init(startRow); },
        error: function() { alert('Error when waiting for Ajax response: ' + arguments[1]); },
        success: function() { 

            $xml = $(arguments[0]);

            var errString  = '';
            $xml.find('error').each(function(i, elem) {
                errString = $(elem).text();
            });

            if (errString != '') {
                alert('Error when running search: ' + errString);
                list_render();
                return false;
            }
            
            $xml.find('max').each(function(i, elem) { 
		maxRows = $(elem).text();
            }); 



            $xml.find('item').each(function(i, elem) { 
                    list_add({
                            context: $(elem).find('context').text(),
                            facility: $(elem).find('facility').text(),
                                from: $(elem).find('from').text(),
                            hostname: $(elem).find('hostname').text(),
                                id: $(elem).find('id').text(),
                            program: $(elem).find('program').text(),
                            severity: $(elem).find('severity').text(),
                                tag: $(elem).find('tag').text(),
                                msg: $(elem).find('msg').text(),
                        timestamp: $(elem).find('timestamp').text()
                    });

            });
            list_render();
            startRow += 100;
        },
        timeout: 60000
    });

}
