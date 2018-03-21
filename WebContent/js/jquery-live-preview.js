/***********************************************
Jquery Live Link Preview Plugin:: Created by Alan Phoon (http://www.ampedupdesigns.com)
This notice MUST stay intact for legal use.
***********************************************/
(function($) {
  $.fn.extend({
     livePreview: function(options){
         var defaults = {
             targetWidth : 1000,
             targetHeight: 800,
             viewWidth: 600,
             viewHeight: 400,
             position: 'right',
             positionOffset: 50
         }
         var options = $.extend(defaults, options);
         //calculate appropriate scaling based on width.
         var scale_w = (options.viewWidth / options.targetWidth);
         var scale_h = ($(window).height() / options.targetHeight);
         var scale_f = 1;
         if(typeof options.scale != 'undefined')
             scale_f = options.scale;
         else
         {
             if(scale_w > scale_h)
                 scale_f = scale_w;
             else
                 scale_f = scale_h;
         }
         return this.each(function() {
            var o = options;
            o.viewHeight = $(window).height();
            var s = scale_f;
            var obj = $(this);
            var href = $(this).attr("href");
            
            obj.hover(function() {
                var pos = $(this).offset();
                var width = $(this).width();
                //var leftpos = pos.left + width + o.positionOffset;
                var leftpos = $(window).width() * .59;
                if(o.position == 'left')
                    leftpos = pos.left - o.viewWidth - o.positionOffset;
                var toppos;
                if($(window).scrollTop() < ($('#Stub').height() + $('#TopBar').height() + $('#BlackBar').height()))
                	toppos = $('#BlackBar').offset().top + $('#TopBar').offset().top + $('#Stub').offset().top + 10;
                else
                	toppos = $(window).scrollTop();
                //hover on 
                $('body').append('<div id="livepreview_dialog" style="display:none; background:#fff; padding:0px; border-left:1px solid #C0C0C0; left: ' 
                		+ leftpos + 'px; top:' + toppos + 'px; width: ' + $(window).width()*.5 + 'px; height: ' 
                		+ o.viewHeight + 'px"><iframe scrolling="no" frameBorder="0" id="livepreview_iframe" src="' 
                		+ href + '" style="border: 0; height:' + o.targetHeight + 'px; width:' + $(window).width()*.5 
                		+ 'px;-moz-transform: scale('+ s + ');-moz-transform-origin: 0 0;-o-transform: scale('+ s + ');-o-transform-origin: 0 0;-webkit-transform: scale('+ s + ');-webkit-transform-origin: 0 0;"></iframe></div>');
                $('#livepreview_dialog').fadeIn(500);
                //animate({width:'toggle'},o.viewWidth);
            },
            function() {
                //hover off
                $('#livepreview_dialog').fadeOut(300, function() {
                    $("#livepreview_dialog").remove();
                });
            });
         });
     }
  });
})(jquery);