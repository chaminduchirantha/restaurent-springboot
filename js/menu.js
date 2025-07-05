$(document).ready(function () {
    function filterCards(category) {
        $('[data-category]').each(function () {
            const categories = $(this).data('category').split(' ');
            if (categories.includes(category)) {
                $(this).show();
            } else {
                $(this).hide();
            }
        });
    }


    $('#filter-breakfast').on('click', function () {
        filterCards('breakfast');
    });

    $('#filter-lunch').on('click', function () {
        filterCards('lunch');
    });

    $('#filter-dinner').on('click', function () {
        filterCards('dinner');
    });

    $('#all').on('click', function () {
        filterCards('all');
    });
});