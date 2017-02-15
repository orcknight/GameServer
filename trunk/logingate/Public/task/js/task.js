'use strict';

$(function () {

    /*
    ** global variables
    */

    var INDEX = {
        params: {
            currPage: 1,
            pageSize: 10,
            pCateId: 1,
            cateId: undefined,
            sort: undefined,
        },
        loading: false,
        maxItems: 100,
        lastIndex: 10,
        hasLoaded: false,
        hasRemoveInfinite: false,
    };

    /*
    ** detail handle for each page
    */

    $(document).on('pageInit', '#status', function () {
        console.log('inited page::', 'status');
    });

    $(document).on('pageInit', '#index', function (e, id, page) {
    	console.log('inited page::', 'index');

        var url = CON_PATH + '/tasks';
        var params = INDEX.params;
        var box = $('.task-list');
        var left = $('#task_type_filter');
        var right = $('#task_sort_filter');
        var filter = $('.filter_2_col');

        if (!INDEX.hasLoaded) {
            addItems();
            INDEX.hasLoaded = true;
        } else if (!box.children('a').length) {
            INDEX.params.currPage = 1;
            addItems();
        }

        page.off('infinite').on('infinite', '.infinite-scroll', function () {
            if (INDEX.loading) return;
            INDEX.loading = true;
            setTimeout(function () {
                if (INDEX.lastIndex >= INDEX.maxItems) {
                    $.detachInfiniteScroll($('.infinite-scroll'));
                    $('.infinite-scroll-preloader').hide();
                    return;
                }

                addItems(true);
                $.refreshScroller();
            }, 100);
        });

        //bind some click events

        $('#refresh_btn').off('click').on('click', function () {
            location.href = CON_PATH + '/index?t=' + new Date().getTime();
        });

        filter.off('click').on('click', '#task_type_filter', function () {
            var self = $(this);
            right.removeClass('hover');
            self.toggleClass('hover');
        });

        filter.on('click', '#task_sort_filter', function () {
            var self = $(this);
            left.removeClass('hover');
            self.toggleClass('hover');
        });

        filter.on('click', 'li[action]', function () {
            var self = $(this), action = self.attr[action], sort = self.data('sort'), type = self.data('type'), text = self.text();
            if (type) {
                left[0].dataset.type = type;
                left.find('span').text(text).addClass('ccto');
            } else if (sort) {
                right[0].dataset.sort = sort;
                right.find('span').text(text).addClass('ccto');
            }

            var typeF = left.data('type'), sortF = right.data('sort');
            INDEX.params.sort = sortF;
            INDEX.params.cateId = typeF;
            INDEX.params.currPage = 1;
            INDEX.hasLoaded = false;
            $('.filter_2_col>a').removeClass('hover');
            $.detachInfiniteScroll($('.infinite-scroll'));
            INDEX.hasRemoveInfinite = true;
            box.empty();
            addItems();
        });

        page.off('click').on('click', function (e) {
            if (!$(e.target).parent('#task_type_filter').length && !$(e.target).parent('#task_sort_filter').length && $(e.target).attr('id') !== 'task_type_filter' && $(e.target).attr('id') !== 'task_sort_filter') {
                $('.filter_2_col>a').removeClass('hover');
            }
        });

        isLogin(function (result) {
            if (result) {
                page.on('click', '.left_part', function (e) {
                    $.router.load(CON_PATH + '/mine', true);
                });
                page.on('click', '.right_part', function (e) {
                    $.router.load(CON_PATH + '/wallet', true);
                });
            } else {
                page.on('click', '.left_part, .right_part', function (e) {
                    $.toast('您还没有登录哦~', 1000);
                    setTimeout(function () {
                        location.href = APP_PATH + '/Home/Index/signIn?toOnline=0';
                    }, 1000);
                });
            }
        });

        function addItems(isInfinite) {
            if (!isInfinite && INDEX.hasLoaded && box.children('a').length) return;

            if (INDEX.hasRemoveInfinite) {
                $.attachInfiniteScroll($('.infinite-scroll'));
                $('.infinite-scroll-preloader').show();
            }

            $.ajax({
                type: 'POST',
                url: url,
                dataType: 'json',
                data: params,
                success: function (res) {
                    if (res.code && res.data) {
                        INDEX.maxItems = res.data.total;
                        if (res.data.data && res.data.data.length) {
                            var items = res.data.data;
                            items.forEach(function (item) {
                                item.price = formatNum(String(item.price));
                                item.end_time = item.end_time.split(' ')[0];
                            });
                            var html = template('tpl-tasks', {items: items});
                            box.append(html);
                            INDEX.lastIndex = box.children('a').length;
                            INDEX.params.currPage++;

                            if (isInfinite) {
                                INDEX.loading = false;
                            }

                            if (!isInfinite && INDEX.lastIndex >= INDEX.maxItems) {
                                $.detachInfiniteScroll($('.infinite-scroll'));
                                $('.infinite-scroll-preloader').hide();
                                return;
                            }
                        } else {
                            $.detachInfiniteScroll($('.infinite-scroll'));
                            INDEX.hasRemoveInfinite = true;
                            $('.infinite-scroll-preloader').hide();
                        }
                    } else {
                        $.detachInfiniteScroll($('.infinite-scroll'));
                        INDEX.hasRemoveInfinite = true;
                        $('.infinite-scroll-preloader').hide();
                    }
                },
                error: server_error
            });
        }

        function isLogin(callback) {
            $.ajax({
                type: 'GET',
                url: APP_PATH + '/Home/User/isLogin',
                dataType: 'json',
                success: function (res) {
                    if (res.code) {
                        callback(true);
                    } else {
                        callback(false);
                    }
                }
            });
        }
    });

    $(document).on('pageInit', '#detail', function (e, id, page) {
    	console.log('inited page::', 'detail');

        var task_id = getParam('task_id', 0), box = $('#wrap_detail'), apply_id, timer, qLink = getParam('qLink', 0);

        if (qLink) {
            $('a.back').removeClass('back');
        }
        page.off('click');

        Promise.all([getDetail(CON_PATH + '/getTaskDetail'), getDetail(APP_PATH + '/Home/User/isLogin')]).then(function (response) {
            var res = response[0], ares = response[1];
            if (res.code) {
                var data = res.data, end_time_copy = data.end_time;
                data.price = formatNum(String(data.price));
                data.end_time = data.end_time.split(' ')[0];
                if (ares.code == 0) {
                    data.nologin = 1;
                } else {
                    data.nologin = 0;
                }
                if (new Date(data.end_time_copy).getTime() < new Date().getTime()) {
                    data.outdate = 1;
                }
                if (data.is_applied && !data.expired_time && data.u_status == 1) {
                    data.u_status = 3;
                }
                var html = template('tpl-task', data);
                box.html(html);
                var html2 = template('tpl-task-button', data);
                $('.wrap-btn').html(html2);
                $('.wrap-detail-summary').html(escapeHTML(data.intro));
                $('.wrap-detail-summary').find('img').each(function (index, item) {
                    var p = $(item).parent('p');
                    if (p.length) {
                        p.css({ display: 'inline-block' });
                    }
                });
                imgsLive('.wrap-detail-summary');
                if (data.nologin === 1) {
                    page.on('click', '.btn-gain', function () {
                        $.toast('您还没有登录哦~', 1000);
                        setTimeout(function () {
                            location.href = APP_PATH + '/Home/Index/signIn?toOnline=' + task_id;
                        }, 1000);
                    });
                    return;
                }
                if (data.is_applied && data.expired_time && data.apply_id) {
                    apply_id = data.apply_id;
                    data.expired_time = data.expired_time.replace(/\-/g, '/');
                    var timeout = new Date(data.expired_time).getTime() - new Date().getTime();
                    timer = handleTime(timeout, $('.countdown'));
                    bindBtnSubmit();
                } else if (!data.is_applied) {
                    bindBtnGain();
                }
            }
        }).catch(function (err) {console.error(err);});

        $(window).off('pageRemoved').on('pageRemoved', function () {
            clearInterval(timer);
        });

        function bindBtnGain() {
            page.on('click', '.btn-gain', function () {
                $.ajax({
                    type: 'POST',
                    url: CON_PATH + '/applyTask',
                    dataType: 'json',
                    data: { id: task_id },
                    success: function (res) {
                        if (res.code) {
                            apply_id = res.data;
                            $.toast('成功领取任务~', 1000);
                            setTimeout(function () {
                                $.router.load(CON_PATH + '/step?task_id=' + task_id + '&apply_id=' + apply_id, true);
                            }, 1000);
                        } else {
                            $.toast(res.msg, 1000);
                        }
                    },
                    error: server_error
                });
            });
        }

        function bindBtnSubmit() {
            page.on('click', '.btn-continue', function () {
                $.router.load(CON_PATH + '/step?task_id=' + task_id + '&apply_id=' + apply_id, true);
            });
        }

        function getDetail(url) {
            return new Promise(function (resolve, reject) {
                $.ajax({
                    type: 'POST',
                    url: url,
                    dataType: 'json',
                    data: { id: task_id },
                    success: function (res) {
                        resolve(res);
                    },
                    error: reject
                });
            });
        }
    });

    var MINE_STATUS = {
        ALL: { value: undefined, sort: 1 },
        ON: { value: '1', sort: 2 },
        CHECK: { value: '2', sort: 3 }, 
        OFF: { value: '3,4,5', sort: 4 },
    };

    $(document).on('pageInit', '#mine', function (e, id, page) {
    	console.log('inited page::', 'mine');

        var box = $('.task-list'), tab = $('.tab-4'), currTab = 1;

        var MINE = {
            params: {
                currPage: 1,
                pageSize: 10,
                uStatus: MINE_STATUS.ALL.value,
            },
            loading: false,
            maxItems: 100,
            lastIndex: 10,
            hasRemoveInfinite: false,
        };

        box.empty();
        addItems();

        page.off('infinite').on('infinite', function () {
            if (MINE.loading) return;
            MINE.loading = true;
            setTimeout(function () {
                if (MINE.lastIndex >= MINE.maxItems) {
                    $.detachInfiniteScroll($('.infinite-scroll'));
                    MINE.hasRemoveInfinite = true;
                    $('.infinite-scroll-preloader').hide();
                    return;
                }

                addItems(true);
                $.refreshScroller();
            }, 100);
        });

        tab.off('click').on('click', 'li[data-status]', function (e) {
            var t = $(e.target), status = t.data('status');
            MINE.params.uStatus = MINE_STATUS[status]['value'];
            currTab = MINE_STATUS[status]['sort'];
            tabSome(currTab);
        });

        bindTouch(page, undefined, 50, {
            left: slideR2L,
            right: slideL2R
        });

        function slideL2R() {
            if (currTab === 1) return;
            currTab--;
            for (var attr in MINE_STATUS) {
                if (MINE_STATUS[attr]['sort'] === currTab) {
                    MINE.params.uStatus = MINE_STATUS[attr]['value'];
                }
            }
            tabSome(currTab, 'l2r');
        }
        function slideR2L() {
            if (currTab === 4) return;
            currTab++;
            for (var attr in MINE_STATUS) {
                if (MINE_STATUS[attr]['sort'] === currTab) {
                    MINE.params.uStatus = MINE_STATUS[attr]['value'];
                }
            }
            tabSome(currTab, 'r2l');
        }
        function tabSome(number, dir) {
            var tempLis = tab.find('li');
            tempLis.each(function (index, item) {
                item.className = '';
            });

            switch (dir) {
                case 'l2r': tempLis.eq(number - 1).addClass('active').addClass('slideR2L'); delayReset(250); break;
                case 'r2l': tempLis.eq(number - 1).addClass('active').addClass('slideL2R'); delayReset(250); break;
                default: tempLis.eq(number - 1).addClass('active'); reset();
            }

            function delayReset(ms) {
                setTimeout(reset, ms);
            }

            function reset() {
                MINE.params.currPage = 1;
                MINE.lastIndex = 10;
                if (MINE.hasRemoveInfinite) {
                    $.attachInfiniteScroll($('.infinite-scroll'));
                    $('.infinite-scroll-preloader').show();
                }
                box.empty();
                addItems(false, true);
                $.detachInfiniteScroll($('.infinite-scroll'));
            }
        }

        function addItems(isInfinite, tabClick) {
            $.ajax({
                type: 'POST',
                url: CON_PATH + '/applys',
                dataType: 'json',
                data: MINE.params,
                success: function (res) {
                    if (res.code && res.data) {
                        MINE.maxItems = res.data.total;
                        var data = res.data.data;
                        data.forEach(function (item) {
                            item.end_time = item.end_time.split(' ')[0];
                        });
                        var html = template('tpl-mine', { items: data });
                        box.append(html);
                        MINE.params.currPage++;
                        MINE.lastIndex = box.children('a').length;

                        if (isInfinite) {
                            MINE.loading = false;
                        }

                        if (tabClick) {
                            $.attachInfiniteScroll($('.infinite-scroll'));
                        }
                        if (!isInfinite && MINE.lastIndex >= MINE.maxItems) {
                            $.detachInfiniteScroll($('.infinite-scroll'));
                            MINE.hasRemoveInfinite = true;
                            $('.infinite-scroll-preloader').hide();
                            return;
                        }
                    } else {
                        $.toast('没有符合条件的兼职哦~', 1000);
                        $.detachInfiniteScroll($('.infinite-scroll'));
                        MINE.hasRemoveInfinite = true;
                        $('.infinite-scroll-preloader').hide();
                    }
                },
                error: server_error
            });
        }
    });

    $(document).on('pageInit', '#wallet', function (e, id, page) {
    	console.log('inited page::', 'wallet');

        var WALLET = {
            params: {
                currPage: 1,
                pageSize: 10,
            },
            loading: false,
            maxItems: 100,
            lastIndex: 10,
        };

        var box = $('.wallet-detail-list'), total = $('#total_income'), today = $('#today_income');

        getSum();

        if ($('.wrap-ani-gif').length) {
            $('.wrap-ani-gif').off('click').on('click', function () {
                $.router.load(CON_PATH + '/index');
            });
        }

        box.empty();
        addItems()

        page.off('infinite').on('infinite', '.infinite-scroll', function () {
            if (WALLET.loading) return;
            WALLET.loading = true;
            setTimeout(function () {
                addItems(true);
                $.refreshScroller();
            }, 100);
        });
    	
        function addItems() {
            $.ajax({
                type: 'POST',
                url: CON_PATH + '/incomeLogs',
                dataType: 'json',
                data: WALLET.params,
                success: function (res) {
                    if (res.code && res.data) {
                        WALLET.maxItems = res.data.total;
                        var items = res.data.data;
                        items.forEach(function (item) {
                            item.money = formatNum(String(item.money));
                            item.create_time = item.create_time.split(' ')[0];
                        });
                        var html = template('tpl-wallet', { items: items });
                        box.append(html);
                        WALLET.lastIndex = box.children('a').length;
                        WALLET.params.currPage++;
                        WALLET.loading = false;

                        if (WALLET.lastIndex >= WALLET.maxItems) {
                            $.detachInfiniteScroll($('.infinite-scroll'));
                            $('.infinite-scroll-preloader').remove();
                            return;
                        }
                    } else {
                        handleLack();
                    }
                },
                error: server_error
            });
        }
        function handleLack() {
            $('.content').append('<div class="nodata"><div class="wrap-ani-gif"><div class="ani-gif"></div><span>您还没有收入<br>快去领取任务吧~</span></div></div>');
            $('.wrap-ani-gif').off('click').on('click', function () {
                $.router.load(CON_PATH + '/index');
            });
        }
        function getSum() {
            $.ajax({
                type: 'GET',
                url: CON_PATH + '/incomeDetail',
                dataType: 'json',
                success: function (res) {
                    if (res.code) {
                        var data = res.data || {};
                        if (data.total_income) {
                            total.html(formatNum(data.total_income));
                        } else {
                            total.html('0.00');
                        }
                        if (data.today_income) {
                            today.html(formatNum(data.today_income));
                        } else {
                            today.html('0.00');
                        }
                    } else {
                        total.html('0.00');
                        today.html('0.00');
                    }
                },
                error: server_error
            });
        }
    });

    $(document).on('pageInit', '#step', function (e, id, page) {
        console.log('inited page::', 'step');
        page.off('click');

        var STEP = null;
        var task_id = getParam('task_id', 0), apply_id = getParam('apply_id', 0), step_id, see_step, curr_step, total_step, type;
        var boxTop = $('.wrap-step-inner section'), boxBottomInner = $('.wrap-form-inner'), boxBottom = $('.step-form');
        var text = $('.text-form'), file = $('.file-form');
        var label = file.find('label'), arrow = $('.fold-step-form');
        var prev = $('.btn-prev-step'), next = $('.btn-next-step');

        var submitObj = {
            applyId: apply_id,
            stepResult: [{
                taskId: task_id,
                stepId: null,
                taskApplyId: apply_id, 
                attr: []
            }]
        };
        var submitArray = null;

        see_step = parseInt(getParam('step', NaN));
        getStepDetail(task_id, apply_id, function () {
            var use_step = !isNaN(see_step) ? see_step : curr_step;
            var currStep = STEP['taskStep'][use_step - 1];
            submitArray = submitObj.stepResult[0].attr;
            submitObj.stepResult[0].stepId = currStep.id;
            type = parseInt(currStep.type);
            boxTop.html(escapeHTML(currStep.intro)); imgsLive(boxTop);
            boxTop.find('img').each(function (index, item) {
                var p = $(item).parent('p');
                if (p.length) {
                    p.css({ display: 'inline-block' });
                }
            });
            var hasNote = currStep.note && currStep.note.trim();
            if (hasNote) {
                boxBottomInner.html('备注: ' + escapeHTML(currStep.note));
            }
            imgsLive(boxBottom);
            bindDelete(boxBottom);

            if (!isNaN(see_step) && (see_step != curr_step)) {
                console.info('you can\'t modify the info.');
            } else {
                if (hasNote || parseInt(currStep.type) !== 1) {
                    boxBottom.addClass('active');
                }
                switch (parseInt(currStep.type)) {
                    case 0: text.show(); file.show(); handleFile(); break;
                    case 1: 
                    if (hasNote) {
                        $('.step-form-title').html('请按要求完成相关操作');
                    }
                    break;
                    case 2: text.show(); break;
                    case 3: file.show(); handleFile();
                }
            }

            if (use_step === total_step) {
                next.text('提交');
            }

            if (use_step === 1) {
                prev.text('任务详情');
            }
            $('a[data-back]').removeClass('back').off('click').on('click', handlePrevStep); //重置返回按钮的功能
        });

        var timer;

        page.on('click', '.fold-step-form', function () {
            if (arrow.hasClass('pending')) {
                boxBottom.removeClass('pending');
                clearTimeout(timer);
                timer = setTimeout(function () {
                    arrow.removeClass('pending');
                }, 200);
            } else {
                boxBottom.addClass('pending');
                clearTimeout(timer);
                timer = setTimeout(function () {
                    arrow.addClass('pending');
                }, 200);
            }
        });
        
        page.on('click', '.btn-next-step', function (event) {

            if (see_step && !isNaN(see_step) && (see_step !== curr_step)) {
                $.router.load(CON_PATH + '/step?task_id=' + task_id + '&apply_id=' + apply_id + '&step=' + (see_step + 1), true);
                return;
            }

            if (!checkInfo(submitArray, type)) {
                $.toast('请将信息填写完整', 1000);
                return false;
            }

            if (type === 0 || type === 2) {
                submitArray.push({ type: '2', content: $('textarea[name="textInfo"]').val() });
            }

            submitArray.forEach(function (item) {
                if (item.origin) delete item.origin;
            });

            $.ajax({
                type: 'POST',
                url: CON_PATH + '/sumbitTask',
                dataType: 'json',
                data: submitObj,
                success: function (res) {
                    if (res.code) {
                        $.toast('提交成功~', 1000);

                        if (res.data.is_finished || curr_step >= total_step) {
                            setTimeout(function () {
                                $.router.load(CON_PATH + '/status');
                            }, 1000);
                            return; 
                        }
                        setTimeout(function () {
                            $.router.load(CON_PATH + '/step?task_id=' + task_id + '&apply_id=' + apply_id + '&flesh_step=' + curr_step);
                        }, 1000);
                    } else {
                        submitArray.pop();
                    }
                }
            });
        });

        page.on('click', '.btn-prev-step', handlePrevStep);

        function handlePrevStep(event) {
            var use_step = !isNaN(see_step) ? see_step : curr_step;

            if (use_step === 1) {
                $.router.load(CON_PATH + '/detail?task_id=' + task_id + '&qLink=1');
            } else {
                $.router.load(CON_PATH + '/step?task_id=' + task_id + '&apply_id=' + apply_id + '&step=' + (use_step - 1));
            }
        }

        function checkInfo(arr, type) {
            
            var text_is_valid = !!$('textarea[name="textInfo"]').val();
            var file_is_valid = arr.some(function (elem) {
                if (parseInt(elem.type) === 3) {
                    return true;
                }
            });
            switch(type) {
                case 0: return text_is_valid && file_is_valid;
                case 1: return true;
                case 2: return text_is_valid;
                case 3: return file_is_valid;
            }
        }

        function handleFile() {
            $('.step_picture').change(function (event) {
                uploadPhoto($('.step_picture')[0], function (data) {
                    submitArray.push({ type: '3', content: data.url, thumb: data.thumburl, origin: data.origin });
                    createImg(submitArray);
                }, function () {
                    $('.form_for_reset')[0].reset();
                });
            });
        }

        function createImg(arr) {
            var item = arr[arr.length - 1];
            var src;
            if (typeof FileReader === 'function') {
                var reader = new FileReader();
                reader.onload = function (event) {
                    src = event.target.result;
                    label.before('<img src="' + src + '" />');
                };
                reader.readAsDataURL(item.origin);
            } else if (item.content && item.content.length) {
                src = item.content;
                label.before('<img src="' + src + '" />');
            }
        }

        function deleteImg(elem) {
            var prev = elem.previousElementSibling, count = 0;
            while (prev) {
                count++;
                prev = prev.previousElementSibling;
            }
            submitArray.splice(count, 1);
            $(elem).remove();
        }

        function bindDelete(dom) {
            bindTouch(boxBottom, 'img', 50, {
                up: function (e) {
                    e.target.classList.add('del');
                    setTimeout(function () {
                        deleteImg(e.target);
                    }, 300);
                }
            }, true);
        }

        function getStepDetail(task_id, apply_id, callback) {
            $.ajax({
                type: 'POST',
                url: CON_PATH + '/getTaskStepDetail',
                dataType: 'json',
                data: {
                    id: task_id,
                    applyId: apply_id
                },
                success: function (res) {
                    var use_step = 0;
                    if (res.code) {
                        STEP = res.data;
                        curr_step = res.data.taskStepResult.length + 1;
                        total_step = res.data.taskStep.length;

                        use_step = !isNaN(see_step) ? see_step : curr_step;

                        if (total_step === 0) {
                            $.toast('该任务尚没有步骤~', 1000);
                            setTimeout(function () {
                                location.href = CON_PATH + '/index';
                            }, 1000);
                        }
                        if (curr_step > total_step) {
                            $.router.load(CON_PATH + '/status');
                            return;
                        }
                        $('.step-count span').text(use_step + '/' + total_step);
                        callback();
                    }
                },
                error: server_error
            });
        }
    });

    //初始化
    $.init();

    /*
    ** ------------------------------common functions---------------------------------------------
    */

    function bindTouch(box, item, distance, callbacks, prevent) {
        var startX, startY;
        var RIGHT = 1, LEFT = 2, UP = 3, DOWN = 4, NOD = 0, attrs = ['up', 'down', 'left', 'right'];
        var targetImg;
        distance || (distance = 50);

        attrs.forEach(function (attr) {
            if (!callbacks[attr]) {
                callbacks[attr] = function () {};
            }
        });

        box.off('touchstart').on('touchstart', item, function (e) {
            targetImg = e.target;
            if (prevent) {
                e.preventDefault();
            }

            startX = e.touches[0].pageX;
            startY = e.touches[0].pageY;
        });

        box.off('touchend').on('touchend', item, function (e) {
            var endX = e.changedTouches[0].pageX, endY = e.changedTouches[0].pageY;
            var direction = getDirection(startX, startY, endX, endY);
            switch(direction) {
                case RIGHT: callbacks['right'](e); break;
                case LEFT: callbacks['left'](e); break;
                case UP: callbacks['up'](e); break;
                case DOWN: callbacks['down'](e); break;
            }
        });

        function getDirection(startX, startY, endX, endY) {
            var dX = endX - startX, dY = endY - startY;
            var result = NOD;

            if (Math.abs(dX) < 2 && Math.abs(dY) < 2 && targetImg.nodeName.toLowerCase() === 'img') {
                targetImg.click();
            }

            if (Math.abs(dX) < distance && Math.abs(dY) < distance) {
                return result;
            }

            var angle = getAngle(dX, dY);

            if (angle >= 45 && angle < 135) {
                result = DOWN;
            } else if ((angle >= 135 && angle < 180) || (angle >= -180 && angle < -135)) {
                result = LEFT;
            } else if (angle >= -135 && angle < -45) {
                result = UP;
            } else if ((angle >= -45 && angle < 0) || (angle >= 0 && angle < 45)) {
                result = RIGHT;
            }
            return result;
        }
        function getAngle(x, y) {
            return Math.atan2(y, x) * 180 / Math.PI;
        }
    }

    function uploadPhoto(input, callback, always) {
        var xhr = new XMLHttpRequest();
        xhr.timeout = 30000; //上传时限为30s
        xhr.responseType = 'json';

        var formData = new FormData();
        var file = input.files[0];
        formData.append('file', file);

        xhr.ontimeout = function () {
            removeProgressTip();
            $.toast('上传超时！');
        };
        xhr.upload.onprogress = uploadProgress;
        xhr.onloadstart = function () {
            console.log('up start!');
            createProgressTip();
        };
        xhr.onload = function () {
            if((xhr.status >= 200 && xhr.status < 300) || xhr.status == 304){
                var res = xhr.response, src;
                if (res.code === 0) {
                    removeProgressTip();
                } else {
                    res.data.origin = file;
                    removeProgressTip();
                    callback(res.data);
                }
            }
        };
        xhr.onabort = removeProgressTip;
        xhr.onerror = removeProgressTip;

        xhr.open('POST', APP_PATH + '/Home/Tools/uploadImage?nosync=true');

        xhr.send(formData);

        return xhr;

        function uploadProgress(event) {
            console.log('up...')
            if (event.lengthComputable) {
                var percentComplete = event.loaded / event.total;
                $('.progress-tip').html(parseInt(percentComplete * 100) + '%');
            }
        }
        function createProgressTip() {
            $('.content').append('<div class="progress-tip"></div>')
        }
        function removeProgressTip() {
            $('.progress-tip').remove();
            always();
        }
    }

    function getParam(str, deflt) {
        var search = decodeURIComponent(location.search);
        var result = new RegExp('[\\W]' + str + '=([0-9]*)').exec(search);
        result = result ? result[1] : deflt;
        return result; 
    }

    function handleTime(time2dead, dom) {
        time2dead = parseInt(time2dead);

        if (time2dead < 0 || time2dead === 0) {
            resetTime();
            return false;
        }
        var HOUR = 3600 * 1000, MINUTE = 60 * 1000, SECOND = 1000;

        var h = Math.floor(time2dead / HOUR); time2dead %= HOUR;
        var m = Math.floor(time2dead / MINUTE); time2dead %= MINUTE;
        var s = Math.floor(time2dead / SECOND);

        setTime(('0' + h).slice(-2), ('0' + m).slice(-2), ('0' + s).slice(-2));

        var time_loop = setInterval(function () {

            s--;
            if (s < 0) {
                m--;
                s = 59;
                if (m < 0) {
                    h--;
                    m = 59;
                    if (h < 0) {
                        resetTime(); clearInterval(time_loop); return;
                    }
                }
            }

            setTime(('0' + h).slice(-2), ('0' + m).slice(-2), ('0' + s).slice(-2));
        }, 1000);

        return time_loop;

        function setTime(h, m, s) {
            dom.html(h + ':' + m + ':' + s);
        }
        function resetTime() {
            var parent = dom.parent().parent();
            parent.html('任务已超时').addClass('btn-banned');
            parent.off('click');
        }
    }

    function formatNum(str){
        var intNum = '', decimal = '', group = [];

        var plusD = function (str) {
            var temp = [], len = str.length;
            for (var i = 0; i < len; i++) {
                var char = str[i];
                if ((len - i - 1) % 3 === 0 && (len - i - 1) !== 0) {
                    temp.push(char, ',');
                } else {
                    temp.push(char);
                }
            }
            return temp.join('');
        };

        if (str = String(str)) {
            group = str.split('.');
            intNum = plusD(group[0]);
            decimal = group[1];
            if (decimal) {
                switch (decimal.length) {
                    case 1: decimal = decimal + '0'; break;
                    default: decimal = decimal.slice(0, 2);
                }
            } else {
                decimal = '00';
            }
            return intNum + '.' + decimal;
        }
    }

    function imgsLive(dom) {
        if (typeof dom === 'string') dom = $(dom);

        dom.off('click').on('click', 'img', function (e) {
            var src = e.target.src;
            var box = $('<div class="img-box"></div>');
            var img = $('<img src="' + src + '" />');

            img.css({
                "width": '100%',
            });
            
            box.html(img);
            document.body.appendChild(box[0]);
            window.addEventListener('popstate', popImg);

            history.pushState(null, '萝卜兼职', '#img');

            box.off('click').on('click', function () {
                box.remove();
                window.removeEventListener('popstate', popImg);
                history.back();
            });

            function popImg() {
                var re = /#img/, url = location.url;
                if (!re.test(url)) {
                    box.remove();
                    window.removeEventListener('popstate', popImg);
                }
            }
        });
    }

    function escapeHTML(str) {
        var entity = {
            quot: '"',
            lt: '<',
            gt: '>',
            nbsp: ' ',
            amp: '&',
        };
        return str.replace(/&([^&;]+);/g, unit).replace(/&([^&;]+);/g, unit); 
        function unit(a, b) {
            var r = entity[b];
            return typeof r === 'string' ? r : a;
        }
    }
});
