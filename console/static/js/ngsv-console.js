var ngsv = ngsv || {};
ngsv.console = ngsv.console || {};

(function() {

    ngsv.console.main = function() {
        $('#bam-upload').change(function() {
            $('#bam-upload-cover').val($(this).val());
        });
        $('#bed-upload').change(function() {
            $('#bed-upload-cover').val($(this).val());
        });

        ngsv.console.initUploader('/api/upload-bam', $('#bam-upload'), $('#bam-upload-btn'), $('#bam-upload-progress'));
        ngsv.console.initUploader('/api/upload-bed', $('#bed-upload'), $('#bed-upload-btn'), $('#bed-upload-progress'));
    };

    ngsv.console.initUploader = function(url, file, button, progress) {
        var up = new uploader(file.get(0), {
            url: url,
			progress: function(ev) {
                var value = (ev.loaded / ev.total) * 100;
                progress.html(Math.floor(value) + '%');
                progress.css('width', value + '%');
            },
			error: function(ev) {
                console.log('error');
            },
			success: function(data) {
                console.log('success');
                progress.html('100%');
                progress.css('width', '100%');
                progress.parent('div').removeClass('progress-striped active');
            }
        });

		button.click(function() {
            progress.parent("div").addClass('progress-striped active');
			up.send();
		});
    };
}());

$(document).ready(ngsv.console.main);
