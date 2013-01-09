var ngsv = ngsv || {};
ngsv.console = ngsv.console || {};

(function() {

    ngsv.console.main = function() {
        $('#bam-upload-select-btn').click(ngsv.console.addBamUploader);
        $('#bed-upload-select-btn').click(ngsv.console.addBedUploader);

        $('.progress .bar').each(function() {
            if ($(this).html() == '100%') {
                $(this).parent('div').removeClass('active');
            } else {
                $(this).parend('div').addClass('active');
            }
        });
    };

    ngsv.console.addBamUploader = function() {
        var id = 0;
        while (1) {
            if ($($.format('#bam-upload_%02d', id)).length == 0)
                break;
            id++;
        };

        var upload = $.format('bam-upload_%02d', id);
        var cover = $.format('bam-upload-cover_%02d', id);
        var btn = $.format('bam-upload-btn_%02d', id);
        var cancel = $.format('bam-upload-cancel_%02d', id);
        var progress = $.format('bam-upload-progress_%02d', id);

        var $e = $($.format('\
<div class="row">\
  <div class="span12 job">\
    <p>Select and upload BAM files</p>\
    <input type="file" id="%s" name="file" style="display: none;">\
    <div class="input-prepend">\
      <a class="btn" onclick="$(\'#%s\').click();">Browse</a>\
      <input id="%s" class="input-large" type="text" placeholder="bam file" autocomplete="off" readonly>\
    </div>\
    <div class="btn-toolbar">\
      <button id="%s" class="btn btn-primary">Upload</button>\
      <button id="%s" class="btn">Cancel</button>\
    </div>\
    <div class="progress progress-striped active">\
      <div id="%s" class="bar" style="width: 0;"></div>\
    </div>\
  </div>\
</div>', upload, upload, cover, btn, cancel, progress));
        $e.prependTo('#new-task').hide().fadeIn(200);

        $('#' + upload).change(function() {
            $('#' + cover).val($(this).val());
        });

        ngsv.console.initUploader('/api/upload-bam', $('#' + upload), $('#' + btn), $('#' + progress));

        $('#' + cancel).click(function() {
            $e.fadeOut(200, function() {
                $(this).remove();
            });
        });
    };

    ngsv.console.addBedUploader = function() {
        var id = 0;
        while (1) {
            if ($($.format('#bed-upload_%02d', id)).length == 0)
                break;
            id++;
        };

        var upload = $.format('bed-upload_%02d', id);
        var cover = $.format('bed-upload-cover_%02d', id);
        var btn = $.format('bed-upload-btn_%02d', id);
        var cancel = $.format('bed-upload-cancel_%02d', id);
        var progress = $.format('bed-upload-progress_%02d', id);

        var $e = $($.format('\
<div class="row">\
  <div class="span12 job">\
    <p>Select and upload Bed file</p>\
    <input type="file" id="%s" name="file" style="display: none;">\
    <div class="input-prepend">\
      <a class="btn" onclick="$(\'#%s\').click();">Browse</a>\
      <input id="%s" class="input-large" type="text" placeholder="bed file" autocomplete="off" readonly>\
    </div>\
    <div class="btn-toolbar">\
      <button id="%s" class="btn btn-primary">Upload</button>\
      <button id="%s" class="btn">Cancel</button>\
    </div>\
    <div class="progress progress-striped active">\
      <div id="%s" class="bar" style="width: 0;"></div>\
    </div>\
  </div>\
</div>', upload, upload, cover, btn, cancel, progress));
        $e.prependTo('#new-task').hide().fadeIn(200);

        $('#' + upload).change(function() {
            $('#' + cover).val($(this).val());
        });

        ngsv.console.initUploader('/api/upload-bed', $('#' + upload), $('#' + btn), $('#' + progress));

        $('#' + cancel).click(function() {
            $e.fadeOut(200, function() {
                $(this).remove();
            });
        });
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
                progress.parent('div').removeClass('active');
            }
        });

		button.click(function() {
            progress.parent("div").addClass('active');
			up.send();
		});
    };
}());

$(document).ready(ngsv.console.main);
