DELETE FROM task WHERE id = 18930;

DELETE FROM task_apply WHERE task_id = 18930;

DELETE FROM task_step WHERE task_id = 18930;

DELETE FROM task_step_result WHERE task_id = 18930;

DELETE FROM task_step_result_attr WHERE result_id IN (
SELECT id FROM task_step_result WHERE task_id = 18930)


DELETE FROM task_step_result WHERE task_id = 18930