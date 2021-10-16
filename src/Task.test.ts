import * as Task from "./Task"
// @ponicode
describe("Task.CheckApps", () => {
    test("0", async () => {
        await Task.CheckApps()
    })
})

// @ponicode
describe("Task.ScheduleTask", () => {
    test("0", async () => {
        await Task.ScheduleTask("Jean-Philippe")
    })

    test("1", async () => {
        await Task.ScheduleTask("Pierre Edouard")
    })

    test("2", async () => {
        await Task.ScheduleTask("Anas")
    })

    test("3", async () => {
        await Task.ScheduleTask("Michael")
    })

    test("4", async () => {
        await Task.ScheduleTask("George")
    })

    test("5", async () => {
        await Task.ScheduleTask("")
    })
})

// @ponicode
describe("Task.BackgroundFetchConfig", () => {
    test("0", () => {
        let callFunction: any = () => {
            Task.BackgroundFetchConfig()
        }
    
        expect(callFunction).not.toThrow()
    })
})
