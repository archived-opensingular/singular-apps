(function () {
    "use strict";

    if (window.FlowButtonsConfigurer === undefined) {

        window.FlowButtonsConfigurer = function () {};

        window.FlowButtonsConfigurer.configure = function () {
            var heights = [];
            var buttons = $('.form-actions  button:visible');
            buttons.each(function () {
                heights.push($(this).position().top)
            });
            if ($.unique(heights).length > 1) {
                var detachedButtons = $('.form-actions .flow-btn:visible:not(.flow-button-dropup)').detach();
                if (detachedButtons.length > 0) {
                    $('.form-actions').append($('<div class="btn-group dropup flow-button-dropup" style="float:right;"><button type="button" class="btn dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><i class="fa fa-cogs"></i> Ações <i class="fa fa-angle-up"></i></button><div class="dropdown-menu"></div></div>'));
                    detachedButtons.appendTo($('.form-actions  .dropdown-menu'));
                }
            }
        }
    }

})();