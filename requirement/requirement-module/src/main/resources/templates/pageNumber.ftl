<style type="text/css">
    body {
        font-family: sans-serif;
        font-size: 10pt;
    }

    #pageCounter {
        position: running(footer);
        text-align: right;
    }

    @page {
        @bottom-right {
            content: element(footer);
        }
    }

    #pagenumber:before {
        content: counter(page);
    }

    #pagecount:before {
        content: counter(pages);
    }
</style>

<div id="pageCounter">
    ${page!} <span id="pagenumber"></span> ${of!} <span id="pagecount"></span>
</div>