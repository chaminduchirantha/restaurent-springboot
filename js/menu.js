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


    let viewCounts = JSON.parse(localStorage.getItem('viewCounts')) || {};

    $(document).ready(function () {
        $('.view-details-btn').click(function () {
            const productId = $(this).data('id');
            const modalId = $(this).data('modal-id');

            // Increment the count
            viewCounts[productId] = (viewCounts[productId] || 0) + 1;

            // Save to localStorage
            localStorage.setItem('viewCounts', JSON.stringify(viewCounts));

            // Find and update the view count inside the correct modal
            const modal = $('#' + modalId);
            modal.find('.view-count[data-id="' + productId + '"]').text(viewCounts[productId]);
        });

        // On page load, update all modal view-count spans with saved data
        $('.view-count').each(function () {
            const id = $(this).data('id');
            if (viewCounts[id]) {
                $(this).text(viewCounts[id]);
            }
        });
    });
});


