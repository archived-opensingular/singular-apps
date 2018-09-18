<style type="text/css">

    /* footer, header - position: fixed */
    #flying-saucer-header {
        position: fixed;
        width: 100%;
        top: 0;
        left: 0;
        right: 0;
    }

    #flying-saucer-footer {
        position: fixed;
        width: 100%;
        bottom: 0;
        left: 0;
        right: 0;
    }

    @page {
        @bottom-right {
            content: element(footer);
        }

    }

    #pageCounter {
        position: running(footer);
        text-align: right;
    }

    #pagenumber:before {
        content: counter(page);
    }

    #pagecount:before {
        content: counter(pages);
    }

</style>

<body>

<div id="pageCounter" style="display: ${showPageNumber!'block'}">
${page!} <span id="pagenumber"></span> ${of!} <span id="pagecount"></span>
</div>

</body>