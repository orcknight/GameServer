<?php

namespace Home\Dao;

class ChiefDao extends BaseDao
{

    public function __construct($db) {

        parent::__construct($db);
    }

    /**
     * 添加头领
     *
     * @param mixed $args
     */
    public function add($args) {

        $this->trackLog("execute", "add()");

        $wxid = $args['wxid'];

        $sql = "INSERT INTO ac_chief ("
            . "wxid,status,realname,tel) VALUES('$wxid',0,'"
            . $args['realname'] . "','" . $args['tel'] . "')";
        $this->trackLog("sql", $sql);
        $res = $this->db->execute($sql);
        $this->trackLog("res", $res);

        $sql = "SELECT last_insert_id() as id";
        $this->trackLog("sql", $sql);
        $result = $this->db->query($sql);
        $this->trackLog('result', $result);

        return $result[0]['id'];
    }

    public function update($args) {

        $setSql = '';
        $whereSql = '';

        if (isset($args['wxid'])) {

            $setSql .= ("wxid='" . $args['wxid'] . "', ");
        }
        if (isset($args['userId'])) {

            $setSql .= ("userId='" . $args['userId'] . "', ");
        }
        if (isset($args['realname'])) {

            $setSql .= ("realname='" . $args['realname'] . "', ");
        }
        if (isset($args['province'])) {

            $setSql .= ("province='" . $args['province'] . "', ");
        }
        if (isset($args['city'])) {

            $setSql .= ("city='" . $args['city'] . "', ");
        }
        if (isset($args['school'])) {

            $setSql .= ("school='" . $args['school'] . "', ");
        }
        if (isset($args['major'])) {

            $setSql .= ("major='" . $args['major'] . "', ");
        }
        if (isset($args['tel'])) {

            $setSql .= ("tel='" . $args['tel'] . "', ");
        }
        if (isset($args['city'])) {

            $setSql .= ("provinceName='" . $args['city'] . "', ");
        }
        if (isset($args['city'])) {

            $setSql .= ("cityName='" . $args['city'] . "', ");
        }
        if (isset($args['city'])) {

            $setSql .= ("schoolName='" . $args['city'] . "', ");
        }
        if (isset($args['path'])) {

            $setSql .= ("qrPath='" . $args['path'] . "', ");
        }
        if (isset($args['register'])) {

            $setSql .= ('register=' . $args['register'] . ', ');
        }

        if (strlen($setSql) > 0) {

            $setSql = rtrim($setSql, ', ');
        }
        if (isset($args['cond_id'])) {

            $whereSql .= ('id=' . $args['cond_id'] . ' AND ');
        }
        if (strlen($whereSql) > 0) {

            $whereSql = rtrim($whereSql, ' AND ');
        }

        $executeSql = "UPDATE ac_chief SET " . $setSql . " WHERE " . $whereSql;
        $this->trackLog("executeSql", $executeSql);
        $result = $this->db->execute($executeSql);
        $this->trackLog("result", $result);

        return $result;
    }

    public function query($args) {

        $this->trackLog("execute", "query()");

        $whereSql = ' 1=1 AND ';
        $limitSql = '';

        if (isset($args['id'])) {

            $whereSql .= ("id='" . $args['id'] . "' AND ");
        }

        if (isset($args['userId'])) {

            $whereSql .= ("userId='" . $args['userId'] . "' AND ");
        }

        if (isset($args['tel'])) {

            $whereSql .= ("tel='" . $args['tel'] . "' AND ");
        }

        if (isset($args['wxid'])) {

            $whereSql .= ("wxid='" . $args['wxid'] . "' AND ");
        }

        if (strlen($whereSql) > 0) {

            $whereSql = rtrim($whereSql, ' AND ');
        }
        if (isset($args['start']) && isset($args['length'])) {

            $limitSql .= (' LIMIT ' . $args['start'] . ', ' . $args['length']);
        }

        $querySql = "SELECT * FROM ac_chief WHERE " . $whereSql . " ORDER BY
        id DESC" . $limitSql;
        $this->trackLog("querySql", $querySql);
        $result = $this->db->query($querySql);

        return $result;
    }

    public function queryFans($args) {

        $this->trackLog("execute", "query()");

        $whereSql = ' 1=1 AND ';
        $limitSql = '';

        if (isset($args['id'])) {

            $whereSql .= ("id='" . $args['id'] . "' AND ");
        }

        if (isset($args['openid'])) {

            $whereSql .= ("openid='" . $args['openid'] . "' AND ");
        }

        if (isset($args['chiefId'])) {

            $whereSql .= ("chiefId='" . $args['chiefId'] . "' AND ");
        }

        if (isset($args['bind'])) {

            $whereSql .= ("bind='" . $args['bind'] . "' AND ");
        }

        if (strlen($whereSql) > 0) {

            $whereSql = rtrim($whereSql, ' AND ');
        }
        if (isset($args['start']) && isset($args['length'])) {

            $limitSql .= (' LIMIT ' . $args['start'] . ', ' . $args['length']);
        }

        $querySql = "SELECT * FROM ac_chief_fans WHERE " . $whereSql . " ORDER BY
        id DESC" . $limitSql;
        $this->trackLog("querySql", $querySql);
        $result = $this->db->query($querySql);

        return $result;
    }

    public function changeNumberById($id, $number) {

        $execSql = "UPDATE ac_chief SET number=number+$number WHERE id = '$id'";
        $this->trackLog("execSql", $execSql);
        $result = $this->getDb()->execute($execSql);
        $this->trackLog("result", $result);

        return $result;
    }

    public function changeRegisterById($id, $register) {

        $execSql = "UPDATE ac_chief SET register=register+$register WHERE id = '$id'";
        $this->trackLog("execSql", $execSql);
        $result = $this->getDb()->execute($execSql);
        $this->trackLog("result", $result);

        return $result;
    }
}


?>
