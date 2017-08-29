<?php

namespace Home\Controller;

use Home\Service\ServerListService;

class ServerListController extends BaseController {
    
    
    public function getServerList() {

        $this->trackLog("execute", "getList()");
        
        $serverListService = new ServerListService();
        $this->ajaxReturn($serverListService->getList());
    }
    
}
?>
