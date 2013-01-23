var console = console || {};
console.viewer = console.viewer || {};

jQuery.extend({
    stringify: function stringify(obj) {
        var t = typeof (obj);
        if (t != "object" || obj === null) {
            // simple data type
            if (t == "string") obj = '"' + obj + '"';
            return String(obj);
        } else {
            // recurse array or object
            var n, v, json = [], arr = (obj && obj.constructor == Array);
 
            for (n in obj) {
                v = obj[n];
                t = typeof(v);
                if (obj.hasOwnProperty(n)) {
                    if (t == "string") v = '"' + v + '"'; else if (t == "object" && v !== null) v = jQuery.stringify(v);
                    json.push((arr ? "" : '"' + n + '":') + String(v));
                }
            }
            return (arr ? "[" : "{") + String(json) + (arr ? "]" : "}");
        }
    }
});

(function() {
    
    console.viewer.ws = undefined;

    console.viewer.main = function() {
        console.viewer.ws = new WebSocket('ws://127.0.0.1:5000/api/ws/send-config')

        $('#viewer-select-btn').click(function() {
            var data = {'bam': [], 'bed': [], 'chromosome': {}};

            $('input:checked').each(function() {
                if ($(this).attr('type') == 'checkbox') {
                    if ($(this).attr('name') == 'bam') {
                        data.bam.push({
                            'name': $(this).attr('name'),
                            'id': $(this).val()
                        });
                    }
                    if ($(this).attr('name') == 'bed') {
                        data.bed.push({
                            'name': $(this).attr('name'),
                            'id': $(this).val()
                        });
                    }
                }
            });

            data.chromosome.name  = $('#chr-select').val();
            data.chromosome.start = $('#chr-start').val();
            data.chromosome.end   = $('#chr-end').val();

            console.viewer.ws.send($.stringify(data));
        });
    };
}());

$(document).ready(console.viewer.main);
